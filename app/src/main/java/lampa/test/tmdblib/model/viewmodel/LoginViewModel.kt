package lampa.test.tmdblib.model.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.App
import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.repository.internet.InternetAuthenticationTmdb
import lampa.test.tmdblib.repository.local.database.LoggedDatabase
import lampa.test.tmdblib.util.constant.FirebaseAuthConstant
import java.lang.Exception

class LoginViewModel: ViewModel(),
    CallBackFromInternetAuthToLoginViewModel {

    private var internetAuthentication: MainContract.InternetAuth
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var userFirebase:FirebaseUser? = null
    private lateinit var databaseListener: ValueEventListener
    private lateinit var firebaseToken: String

    private var db: LoggedDatabase
    private val context: Context = App.contextComponent.getContext()
    private val firebaseAuthConstant = FirebaseAuthConstant()

    private val liveUserData: MutableLiveData<UserData> = MutableLiveData()
    private val liveStatus: MutableLiveData<String> = MutableLiveData()
    private val liveError: MutableLiveData<Exception> = MutableLiveData()
    private val liveIsUserLogIn: MutableLiveData<Boolean> = MutableLiveData()

    private var session: String? = null

    private lateinit var databaseSubscribe: Disposable

    init {
        db = LoggedDatabase.getInstance(context)
        internetAuthentication = InternetAuthenticationTmdb(this)
        initialize()
    }

    fun initialize(){
        databaseSubscribe = db.loggedInUserDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { dbUser ->
                if(dbUser?.size!! > 0) {
                    val dbVal = dbUser[0]
                    session = dbVal!!.session_id

                    liveIsUserLogIn.postValue(true)

                    firebaseToken = dbVal.token

                    if(firebaseAuth.currentUser != null)
                        liveUserData.postValue(createUserData(dbVal.login,dbVal.signInMethod,dbVal.token))
                }
            }, { error("Error db user init (loginViewModel)")})
    }

    fun getUser() = liveUserData
    fun getStatus() = liveStatus
    fun getError() = liveError
    fun getIsLogIn() = liveIsUserLogIn

    fun signUpFirebaseByGoogle(userData: GoogleSignInAccount){
        databaseSubscribe.dispose()

        firebaseToken = userData.idToken!!
        Log.v("112233",firebaseToken)
        firebaseSignInByGoogleCredential()
    }

    fun firebaseSignInByPhone(verificationId: String, smsCode: String){
        databaseSubscribe.dispose()
        liveStatus.postValue("проверка кода")

        val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInUser = GoogleSignIn.getLastSignedInAccount(context)
                    firebaseToken = signInUser?.idToken!!
                    Log.v("112233",firebaseToken)
                    saveDataInBdAndDb()
                } else {

                    liveError.postValue(task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
    }

    private fun firebaseSignInByGoogleCredential(){
        liveStatus.postValue("Аунтификация через гугл аккаунт")

        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(firebaseToken, null))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveDataInBdAndDb()
                } else {
                    liveError.postValue(task.exception)
                }
            }
    }

    private fun saveDataInBdAndDb(){

        val user = firebaseAuth.currentUser

        databaseListener = (object:ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                liveError.postValue(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (userFirebase == null) {
                    if (snapshot.getValue() != null) {
                        userFirebase = user
                        session = snapshot.getValue().toString()

                        val email: String
                        if(userFirebase?.email!=null)
                        {
                            email = userFirebase?.email!!
                            createLogInUserInDb(email, firebaseToken,firebaseAuthConstant.AUTH_WITH_GOOGLE)
                        }
                        else {
                            email = userFirebase?.phoneNumber!!

                            createLogInUserInDb(email, firebaseToken,firebaseAuthConstant.AUTH_WITH_PHONE)
                        }

                    } else {
                        liveStatus.postValue("создание аккаунта")
                        userFirebase = user
                        internetAuthentication.startAuth()
                    }
                }
            }
        })

        firebaseDatabase.getReference(user!!.uid).addValueEventListener(databaseListener)
    }

    private fun createLogInUserInDb(email: String, token: String, auth_with: Int){

        liveStatus.postValue("Сохранение пользователя")
        val userData = createUserData(email,auth_with,token)

        db.loggedInUserDao().delete()
            .subscribeOn(Schedulers.io())
            .subscribe({ db.loggedInUserDao().insert(userData.toDatabaseFormat())
                .subscribeOn(Schedulers.io())
                .subscribe()})

        liveUserData.postValue(userData)
    }

    private fun createUserData(email: String, auth_with: Int, token: String) : UserData = UserData(

        email,
        "",
        auth_with,
        token,
        session!!
    )

    override fun onAuthenticationTmdbSuccess(session_id: String) {

        session = session_id
        firebaseDatabase.getReference(userFirebase?.uid!!).setValue(session)

        val email: String
        if (userFirebase?.email != null)
            email = userFirebase?.email!!
        else
            email = userFirebase?.phoneNumber!!

        createLogInUserInDb(email, firebaseToken, firebaseAuthConstant.AUTH_WITH_GOOGLE)
    }
}
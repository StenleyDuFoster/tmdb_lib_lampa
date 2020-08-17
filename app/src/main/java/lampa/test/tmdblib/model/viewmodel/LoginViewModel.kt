package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.repository.internet.InternetAuthenticationTmdb
import lampa.test.tmdblib.repository.local.database.LoggedDatabase
import lampa.test.tmdblib.utils.constant.FirebaseAuthConstant
import java.lang.Exception

class LoginViewModel(application: Application) : AndroidViewModel(application),
    CallBackFromInternetAuthToLoginViewModel {

    private var internetAuthentication: MainContract.InternetAuth
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var userFirebase:FirebaseUser? = null
    private lateinit var databaseListener: ValueEventListener
    private lateinit var firebaseToken: String

    private var db: LoggedDatabase
    val context: Context = application.applicationContext

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
                    firebaseSignIn()
                }
            }, { error("Error db user init (loginViewModel)")})
    }

    fun getUser() = liveUserData
    fun getStatus() = liveStatus
    fun getError() = liveError
    fun getIsLogIn() = liveIsUserLogIn

    fun signUpFirebase(userData: GoogleSignInAccount){
        databaseSubscribe.dispose()

        firebaseToken = userData.idToken!!
        firebaseSignIn()
    }

    private fun firebaseSignIn(){

        val credential = GoogleAuthProvider.getCredential(firebaseToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
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
                                    createLogInUser(user?.email!!, firebaseToken)
                                } else {
                                    userFirebase = user
                                    internetAuthentication.startAuth()
                                }
                            }
                        }
                    })

                    firebaseDatabase.getReference(user!!.uid).addValueEventListener(databaseListener)
                } else {
                    liveError.postValue(task.exception)
                }
            }
    }

    private fun createLogInUser(email: String, token: String){

        val userData = UserData(

            email,
            "",
            FirebaseAuthConstant().AUTH_WITH_GOOGLE,
            token,
            session!!
        )

        Completable.fromAction {
            db.loggedInUserDao().delete()
            db.loggedInUserDao().insert(userData.toDatabaseFormat())
        }.subscribeOn(Schedulers.io())
              .subscribe()

        liveUserData.postValue(userData)
    }

    override fun onAuthenticationTmdbSuccess(session_id: String) {

        session = session_id
        firebaseDatabase.getReference(userFirebase?.uid!!).setValue(session)
        createLogInUser(userFirebase?.email!!, firebaseToken)
    }
}
package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.Log.d
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.repository.internet.InternetAuthenticationTmdb
import lampa.test.tmdblib.repository.local.database.LoggedDatabase


class LoginViewModel(application: Application) : AndroidViewModel(application),
    CallBackFromInternetAuthToLoginViewModel {

    private lateinit var internetAuthentication: MainContract.InternetAuth
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var userFirebase:FirebaseUser

    private var db: LoggedDatabase
    val context: Context = application.applicationContext

    private val liveUserData: MutableLiveData<UserData> = MutableLiveData()
    private val liveStatus: MutableLiveData<String> = MutableLiveData()
    private val liveError: MutableLiveData<String> = MutableLiveData()
    private val liveIsUserLogIn: MutableLiveData<Boolean> = MutableLiveData()

    private var authenticationUser: String? = null
    private var session: String? = null

    val databaseSubscribe: Disposable

    init {

        db = LoggedDatabase.getInstance(context)
        databaseSubscribe = db.loggedInUserDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { dbUser ->
                if(dbUser?.size!! > 0) {
                    val dbVal = dbUser?.get(0)
                    val user = UserData(
                        dbVal?.login!!,
                        dbVal.password,
                        dbVal.token,
                        true,
                        dbVal.session_id
                    )
                    liveIsUserLogIn.postValue(true)
                    signUpFirebase(user)
                }
            }, { error("Error db user init (loginViewModel)")})
    }

    fun getUser() = liveUserData
    fun getStatus() = liveStatus
    fun getError() = liveError
    fun getIsLogIn() = liveIsUserLogIn

    fun signUpFirebase(userData: UserData){
        databaseSubscribe.dispose()
        liveStatus.postValue("проверка пользователя")
        firebaseAuth.createUserWithEmailAndPassword(userData.name, userData.pass)
            .addOnSuccessListener {
                liveStatus.postValue("ожидание подтверждения почты")
                internetAuthentication = InternetAuthenticationTmdb(this)
                userFirebase = firebaseAuth.currentUser!!
                authenticationUser = userData.name
                userData.session = session.toString()
                userFirebase.sendEmailVerification()
                    .addOnCompleteListener {
                        Thread(
                            Runnable {
                                waitEmailVerification(userData, userFirebase)
                            }
                        ).start()
                }
            }.addOnFailureListener {t->
                firebaseAuth.signInWithEmailAndPassword(userData.name, userData.pass)
                   .addOnSuccessListener {
                       authenticationUser = userData.name

                       firebaseDatabase.getReference(firebaseAuth.currentUser?.uid!!).addValueEventListener(
                           object : ValueEventListener {
                               override fun onCancelled(databaseError: DatabaseError) { }

                               override fun onDataChange(dataSnapshot: DataSnapshot) {
                                   val logInUser = userData
                                   logInUser.session = dataSnapshot.getValue().toString()

                                   createLogInUser(logInUser)
                                   liveUserData.postValue(logInUser)
                               }
                           }
                       )

                   }.addOnFailureListener {ex->
                        liveError.postValue(ex.message.toString())
                   }
            }
    }

    private fun waitEmailVerification(userData: UserData, userFirebase: FirebaseUser?){

        userFirebase?.reload()
        if(userFirebase?.isEmailVerified!!)
        {
            val newUserData = userData
            newUserData.session = session.toString()
            liveStatus.postValue(null)
            liveUserData.postValue(newUserData)
            Log.v("112233","in3")
            db.loggedInUserDao().delete()
            db.loggedInUserDao().insert(newUserData.toDatabaseFormat())
        }
        else {
            Thread.sleep(1000)
            waitEmailVerification(userData, userFirebase)
        }
    }

    private fun createLogInUser(userData:UserData){

        Completable.fromAction(
            {
                db.loggedInUserDao().delete()
                db.loggedInUserDao().insert(userData.toDatabaseFormat())
            }
        )
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onAuthenticationTmdbSuccess(session_id: String) {

        firebaseDatabase.getReference(userFirebase.uid).setValue(session_id)
        this.session = session_id
    }
}
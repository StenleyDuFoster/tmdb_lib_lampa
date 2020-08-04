package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.data.UserData
import lampa.test.tmdblib.model.repository.internet.InternetAuthentication
import lampa.test.tmdblib.model.repository.local.database.LoggedDatabase

class LoginViewModel(application: Application) : AndroidViewModel(application),
    CallBackFromInternetAuthToLoginViewModel {

    var internetAuthentication: MainContract.InternetAuth
    var auth = FirebaseAuth.getInstance()
    lateinit var userFirebase:FirebaseUser

    var db: LoggedDatabase
    val context = application.applicationContext

    val liveUserData: MutableLiveData<UserData> = MutableLiveData()
    val liveStatus: MutableLiveData<Boolean> = MutableLiveData()

    var authenticationUser: String? = null
    var session: String? = null

    init {

        db = Room.databaseBuilder(context, LoggedDatabase::class.java, "database_login")
            .build()

        internetAuthentication = InternetAuthentication(this)

        Thread(
            Runnable {

                if(db.loggedInUserDao().getAll()?.size!! > 0) {
                    val dbVal = db.loggedInUserDao().getAll()!!.get(0)
                    val user = UserData(
                        dbVal?.login!!,
                        dbVal?.password!!,
                        dbVal?.token!!,
                        true,
                        dbVal?.session_id!!
                    )

                    signUpFirebase(user)
                }
            }
        ).start()
    }

    fun getUser() = liveUserData
    fun getStatus() = liveStatus

    fun signUpFirebase(userData: UserData){

        liveStatus.postValue(false)
        auth.createUserWithEmailAndPassword(userData.name, userData.pass)
            .addOnSuccessListener {

                userFirebase = auth.currentUser!!
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
            }.addOnFailureListener { f ->

               auth.signInWithEmailAndPassword(userData.name, userData.pass)
                   .addOnSuccessListener {
                       authenticationUser = userData.name
                       liveUserData.postValue(userData)
                       Thread(
                           Runnable {
                               db.clearAllTables()
                               db.loggedInUserDao().insert(userData.toDatabaseFormat())
                           }
                       ).start()
                   }.addOnFailureListener {
                       liveStatus.postValue(true)
                   }
            }
    }

    fun waitEmailVerification(userData: UserData, userFirebase: FirebaseUser?){

        userFirebase?.reload()
        if(userFirebase?.isEmailVerified!!)
        {
            liveUserData.postValue(userData)
            db.clearAllTables()
            db.loggedInUserDao().insert(userData.toDatabaseFormat())
        }
        else{
            Thread.sleep(5000)
            waitEmailVerification(userData, userFirebase)
        }
    }

    override fun onAuthenticationTmdbSuccess(session: String) {

        this.session = session
    }
}
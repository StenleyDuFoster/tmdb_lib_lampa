package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.data.User
import lampa.test.tmdblib.model.repository.internet.InternetAuthentication
import lampa.test.tmdblib.model.repository.local.database.LoggedDatabase

class LoginViewModel(application: Application) : AndroidViewModel(application),
    CallBackFromInternetAuthToLoginViewModel {

    var internetAuthentication: MainContract.InternetAuth
    var auth = FirebaseAuth.getInstance()
    lateinit var userFirebase:FirebaseUser

    var db: LoggedDatabase
    val context = application.applicationContext

    val liveUser: MutableLiveData<User> = MutableLiveData()
    val liveStatus: MutableLiveData<Boolean> = MutableLiveData()

    var auntificateUser: String? = null
    var session: String? = null

    init {

        db = Room.databaseBuilder(context, LoggedDatabase::class.java, "database_login")
            .build()

        internetAuthentication = InternetAuthentication(this)

        Thread(
            Runnable {

                if(db.loggedInUserDao().getAll()?.size!! > 0) {
                    val dbVal = db.loggedInUserDao().getAll()!!.get(0)
                    val user = User(
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

    fun getUser() = liveUser
    fun getStatus() = liveStatus

    fun signUpFirebase(user: User){

        liveStatus.postValue(false)
        auth.createUserWithEmailAndPassword(user.name, user.pass)
            .addOnSuccessListener {

                userFirebase = auth.currentUser!!
                auntificateUser = user.name
                user.session = session.toString()

                userFirebase.sendEmailVerification()
                    .addOnCompleteListener {
                    Thread(
                        Runnable {
                            waitEmailVerification(user, userFirebase)
                        }
                    ).start()
                }
            }.addOnFailureListener { f ->

               auth.signInWithEmailAndPassword(user.name, user.pass)
                   .addOnSuccessListener {
                       auntificateUser = user.name
                       liveUser.postValue(user)
                       db.clearAllTables()
                       db.loggedInUserDao().insert(user.toDatabaseFormat())
                   }.addOnFailureListener {
                       liveStatus.postValue(true)
                   }
            }
    }

    fun waitEmailVerification(user: User, userFirebase: FirebaseUser?){

        userFirebase?.reload()
        if(userFirebase?.isEmailVerified!!)
        {
            liveUser.postValue(user)
            db.clearAllTables()
            db.loggedInUserDao().insert(user.toDatabaseFormat())
        }
        else{
            Thread.sleep(5000)
            waitEmailVerification(user, userFirebase)
        }
    }

    override fun onAuthenticationTmdbSuccess(session: String) {

        this.session = session
    }
}
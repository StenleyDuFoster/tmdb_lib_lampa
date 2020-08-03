package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.data.User
import lampa.test.tmdblib.model.repository.internet.InternetAuthentication
import lampa.test.tmdblib.model.repository.local.database.LoggedDatabase

class LoginViewModel(application: Application) : AndroidViewModel(application),
    CallBackFromInternetAuthToLoginViewModel {

    lateinit var internetAuntificate: MainContract.InternetAuth

    var db: LoggedDatabase
    val context = application.applicationContext

    val liveUser: MutableLiveData<User> = MutableLiveData()
    val liveStatus: MutableLiveData<String> = MutableLiveData()

    lateinit var log: String
    lateinit var pass: String

    init {

        db = Room.databaseBuilder(context, LoggedDatabase::class.java, "database_login")
            .build()

        internetAuntificate = InternetAuthentication(this)

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
                    liveUser.postValue(user)
                }
            }
        ).start()
    }

    fun getUser() = liveUser

    fun createUser(log:String, pass: String){

        this.log = log
        this.pass = pass

        internetAuntificate.createSession()

    }

    override fun onAuthenticationSuccess(session: String) {

        Thread(
            Runnable {

                val user =  User(
                    log,
                    pass,
                    "",
                    true,
                    session
                )

                db.loggedInUserDao().insert(
                    user.toDatabaseFormat()
                )

                liveUser.postValue(user)
            }
        ).start()
    }
}
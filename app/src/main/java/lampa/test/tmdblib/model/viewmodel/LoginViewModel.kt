package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    private var authenticationUser: String? = null
    private var session: String? = null

    init {

        db = Room.databaseBuilder(context, LoggedDatabase::class.java, "database_login")
            .build()

        Thread(
            Runnable {

                if(db.loggedInUserDao().getAll()?.size!! > 0) {
                    val dbVal = db.loggedInUserDao().getAll()!![0]
                    val user = UserData(
                        dbVal?.login!!,
                        dbVal.password,
                        dbVal.token,
                        true,
                        dbVal.session_id
                    )

                    signUpFirebase(user)
                }
            }
        ).start()
    }

    fun getUser() = liveUserData
    fun getStatus() = liveStatus

    fun signUpFirebase(userData: UserData){

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
            }.addOnFailureListener {

                firebaseAuth.signInWithEmailAndPassword(userData.name, userData.pass)
                   .addOnSuccessListener {
                       authenticationUser = userData.name
                       liveUserData.postValue(userData)

                       firebaseDatabase.getReference(firebaseAuth.currentUser?.uid!!).addValueEventListener(
                           object : ValueEventListener{
                               override fun onCancelled(p0: DatabaseError) {
                                   TODO("Not yet implemented")
                               }

                               override fun onDataChange(p0: DataSnapshot) {
                                   val logInUser = userData
                                   logInUser.session = p0.getValue().toString()

                                   Thread(
                                       Runnable {
                                           createLogInUser(logInUser)
                                           liveUserData.postValue(logInUser)
                                       }
                                   ).start()
                               }
                           }
                       )

                   }.addOnFailureListener {
                        liveStatus.postValue("пользователь не найден или данные не верны")
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
            db.clearAllTables()
            db.loggedInUserDao().insert(newUserData.toDatabaseFormat())
        }
        else {
            Thread.sleep(1000)
            waitEmailVerification(userData, userFirebase)
        }
    }

    private fun createLogInUser(userData:UserData){

        db.clearAllTables()
        db.loggedInUserDao().insert(userData.toDatabaseFormat())
    }

    override fun onAuthenticationTmdbSuccess(session_id: String) {

        firebaseDatabase.getReference(userFirebase.uid).setValue(session_id)
        this.session = session_id
    }
}
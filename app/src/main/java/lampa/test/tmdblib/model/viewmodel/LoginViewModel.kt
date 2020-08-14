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
import java.lang.Exception


class LoginViewModel(application: Application) : AndroidViewModel(application),
    CallBackFromInternetAuthToLoginViewModel {

    private lateinit var internetAuthentication: MainContract.InternetAuth
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var userFirebase:FirebaseUser

    private lateinit var db: LoggedDatabase
    val context: Context = application.applicationContext

    private val liveUserData: MutableLiveData<UserData> = MutableLiveData()
    private val liveStatus: MutableLiveData<String> = MutableLiveData()
    private val liveError: MutableLiveData<Exception> = MutableLiveData()
    private val liveIsUserLogIn: MutableLiveData<Boolean> = MutableLiveData()

    private var authenticationUser: String? = null
    private var session: String? = null

    private lateinit var databaseSubscribe: Disposable

    init {
        //initialize()
    }

    fun initialize(){
        databaseSubscribe = db.loggedInUserDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { dbUser ->
                if(dbUser?.size!! > 0) {
                    val dbVal = dbUser[0]
                    val user = UserData(
                        dbVal?.login!!,
                        dbVal.password,
                        dbVal.token,
                        true,
                        dbVal.session_id
                    )
                    liveIsUserLogIn.postValue(true)
  //                  signUpFirebase(user)
                }
            }, { error("Error db user init (loginViewModel)")})
    }

    fun getUser() = liveUserData
    fun getStatus() = liveStatus
    fun getError() = liveError
    fun getIsLogIn() = liveIsUserLogIn

    fun signUpFirebase(userData: GoogleSignInAccount){
        //databaseSubscribe.dispose()

        val credential = GoogleAuthProvider.getCredential("908013122312-3hh0gomo06m044csu4vbc3dvde7eospm.apps.googleusercontent.com", null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    // ...
                }

                // ...
            }

    }

    private fun createLogInUser(userData:UserData){

        Completable.fromAction {
            db.loggedInUserDao().delete()
            db.loggedInUserDao().insert(userData.toDatabaseFormat())
        }.subscribeOn(Schedulers.io())
              .subscribe()
    }

    override fun onAuthenticationTmdbSuccess(session_id: String) {

        firebaseDatabase.getReference(userFirebase.uid).setValue(session_id)
        this.session = session_id
    }
}
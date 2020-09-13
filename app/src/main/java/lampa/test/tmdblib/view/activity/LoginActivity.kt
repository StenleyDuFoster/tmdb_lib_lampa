package lampa.test.tmdblib.view.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.repository.data.UserData
import lampa.test.tmdblib.util.toast.makeToast
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.FragmentLogin
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity

class LoginActivity : BaseActivity(), CallBackFromLoginFToActivity {

    private val loginFragment = FragmentLogin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_login)

        addFragmentToFragmentManager(loginFragment)
    }

    override fun userLogin(userData: UserData) {

        val intentToMAinActivity = Intent(this, MainActivity::class.java)
        intentToMAinActivity.putExtra("session_id", userData.session_id)
        startActivity(intentToMAinActivity)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                loginFragment.loginViewModel.signUpFirebaseByGoogle(account)

            } catch (e: ApiException) {
                makeToast(e.message!!)
            }
    }
}
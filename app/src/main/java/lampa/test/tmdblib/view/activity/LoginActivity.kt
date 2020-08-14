package lampa.test.tmdblib.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import lampa.test.tmdblib.R
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.FragmentLogin
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity

class LoginActivity : BaseActivity(), CallBackFromLoginFToActivity {

    val loginFragment = FragmentLogin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initFragment()
    }

    fun initFragment(){

        addFragmentToFragmentManager(loginFragment)
    }

    override fun userLogin(userData: UserData) {

        val intentToMAinActivity = Intent(this, MainActivity::class.java)
        intentToMAinActivity.putExtra("session_id", userData.session)
        startActivity(intentToMAinActivity)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                loginFragment.loginViewModel.signUpFirebase(account)

            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }
}
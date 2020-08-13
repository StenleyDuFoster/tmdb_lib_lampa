package lampa.test.tmdblib.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import lampa.test.tmdblib.R
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.FragmentLogin
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity

class LoginActivity : BaseActivity(), CallBackFromLoginFToActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initFragment()
    }

    fun initFragment(){

        val fragmentSignInOrRegister = FragmentLogin()
        addFragmentToFragmentManager(R.id.content_container, fragmentSignInOrRegister)
    }

    override fun userLogin(userData: UserData) {

        val intentToMAinActivity = Intent(this, MainActivity::class.java)
        intentToMAinActivity.putExtra("session_id", userData.session)
        startActivity(intentToMAinActivity)
        finish()
    }
}
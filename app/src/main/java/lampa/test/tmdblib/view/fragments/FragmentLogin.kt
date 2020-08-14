package lampa.test.tmdblib.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.login_fragment.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.LoginViewModel
import lampa.test.tmdblib.utils.anim.CustomAnimate
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.base.BaseFragment
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity


@Suppress("PLUGIN_WARNING")
class FragmentLogin : BaseFragment(R.layout.login_fragment) {

    var alpha: Float = 1f
    lateinit var loginViewModel: LoginViewModel
    private lateinit var callbackToActivity: CallBackFromLoginFToActivity
    var animateClass = CustomAnimate()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        callbackToActivity = activity as CallBackFromLoginFToActivity
        view.setBackgroundColor(Color.WHITE)

        val clickListenerButtonLog = View.OnClickListener {

            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(("908013122312-3hh0gomo06m044csu4vbc3dvde7eospm.apps.googleusercontent.com"))
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(activity!!, signInOptions)
            val signInIntent: Intent = signInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)
        }

        google_button.setOnClickListener(clickListenerButtonLog)
        google_button.isClickable = true

        loginViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                callbackToActivity.userLogin(user)
        })

        loginViewModel.getError().observe(viewLifecycleOwner, Observer { error ->

            view.setBackgroundColor(Color.WHITE)

            if(error.message.toString().contains("network"))
            {
                val loginDatabase = Runnable {
                    loginViewModel.initialize()
                }

                (activity!! as BaseActivity).networkChangeReceiver.setRunnableCode(loginDatabase)
            }
        })

        loginViewModel.getIsLogIn().observe(viewLifecycleOwner, Observer {
            alpha = 0f
            view.setBackgroundColor(Color.TRANSPARENT)
            initAlpha()
        })

        loginViewModel.getStatus().observe(viewLifecycleOwner, Observer { msg ->

            if(msg != null) {
                animateClass.alphaFadeIn(layLoading)
                animateClass.alphaFadeOut(materialCardView)
                animateClass.alphaFadeOut(buttonLog)
                textLoading.text = msg
            }else {
                animateClass.alphaFadeOut(layLoading)
            }
        })
    }

    fun initAlpha(){

    }
}
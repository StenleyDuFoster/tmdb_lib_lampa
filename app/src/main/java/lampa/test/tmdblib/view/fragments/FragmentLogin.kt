package lampa.test.tmdblib.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.login_fragment.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.LoginViewModel
import lampa.test.tmdblib.utils.anim.CustomAnimate
import lampa.test.tmdblib.utils.constant.FirebaseAuthConstant
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.base.BaseFragment
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity


@Suppress("PLUGIN_WARNING")
class FragmentLogin : BaseFragment(R.layout.login_fragment) {

    var alpha: Float = 1f
    lateinit var loginViewModel: LoginViewModel
    private lateinit var callbackToActivity: CallBackFromLoginFToActivity
    private var animateClass = CustomAnimate()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        callbackToActivity = activity as CallBackFromLoginFToActivity
        view.setBackgroundColor(Color.WHITE)

        val clickListenerButtonLog = View.OnClickListener {

            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(FirebaseAuthConstant().AUTH_GOOGLE_TOKEN)
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(activity!!, signInOptions)
            val signInIntent: Intent = signInClient.signInIntent
            startActivityForResult(signInIntent, 1)
            animateClass.alphaFadeIn(layLoading)
            google_button.isClickable = false
            animateClass.alphaFadeOut(google_button)
        }

        google_button.setOnClickListener(clickListenerButtonLog)
        google_button.isClickable = true

        loginViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                callbackToActivity.userLogin(user)
        })

        loginViewModel.getError().observe(viewLifecycleOwner, Observer { error ->

            google_button.isClickable = true
            animateClass.alphaFadeIn(google_button)
            view.setBackgroundColor(Color.WHITE)
            Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            animateClass.alphaFadeOut(layLoading)

            if(error.message.toString().contains("network"))
            {
                val loginDatabase = Runnable {
                    loginViewModel.initialize()
                }

                (activity!! as BaseActivity).networkChangeReceiver.setRunnableCode(loginDatabase)
            }
        })

        loginViewModel.getIsLogIn().observe(viewLifecycleOwner, Observer {
            animateClass.alphaFadeIn(layLoading)
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

    private fun initAlpha(){

        google_button.alpha = alpha
        mainLogoImage.alpha = alpha
    }
}
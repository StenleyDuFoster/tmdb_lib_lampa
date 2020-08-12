package lampa.test.tmdblib.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import kotlinx.android.synthetic.main.login_fragment.*

import lampa.test.tmdblib.R
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.model.viewmodel.LoginViewModel
import lampa.test.tmdblib.utils.anim.CustomAnimate
import lampa.test.tmdblib.view.fragments.base.BaseFragment

@Suppress("PLUGIN_WARNING")
class FragmentLogin : BaseFragment(R.layout.login_fragment) {

    var alpha: Float = 1f
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var callbackToActivity: CallBackFromLoginFToActivity
    var animateClass = CustomAnimate()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginViewModel = ViewModelProvider(activity!!).get(LoginViewModel::class.java)
        callbackToActivity = activity as CallBackFromLoginFToActivity
        view.setBackgroundColor(Color.WHITE)

        val clickListenerButtonLog = View.OnClickListener {
            if(nameEdit.length()>0 && passEdit.length()>0) {

                loginViewModel.signUpFirebase(
                    UserData(
                        name = nameEdit.text.toString(),
                        pass = passEdit.text.toString(),
                        token = "",
                        signIn = true,
                        session = ""
                    )
                )
                animateClass.alphaFadeOut(materialCardView)
                animateClass.alphaFadeOut(buttonLog)
                buttonLog.isClickable = false
                nameEdit.isClickable = false
                passEdit.isClickable = false
            } else
                nameEdit.setError("login and password should not be empty")
        }

        val buttonLog = view.findViewById<Button>(R.id.buttonLog)
        buttonLog.setOnClickListener(clickListenerButtonLog)
        buttonLog.isClickable = true

        loginViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                callbackToActivity.userLogin(user)
        })

        loginViewModel.getError().observe(viewLifecycleOwner, Observer { error ->
            animateClass.alphaFadeIn(materialCardView)
            animateClass.alphaFadeIn(buttonLog)
            animateClass.alphaFadeOut(layLoading)
            buttonLog.isClickable = true
            nameEdit.isClickable = true
            passEdit.isClickable = true
            nameEdit.setError(error)
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

        materialCardView.alpha = alpha
        buttonLog.alpha = alpha
        mainLogoImage.alpha = alpha
    }
}
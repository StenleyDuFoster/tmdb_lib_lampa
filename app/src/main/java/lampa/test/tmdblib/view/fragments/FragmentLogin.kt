package lampa.test.tmdblib.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.login_fragment.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.model.viewmodel.LoginViewModel
import lampa.test.tmdblib.utils.anim.CustomAnimate

@Suppress("PLUGIN_WARNING")
class FragmentLogin : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var callbackToActivity: CallBackFromLoginFToActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(activity!!).get(LoginViewModel::class.java)
        callbackToActivity = activity as CallBackFromLoginFToActivity
        val v = inflater.inflate(R.layout.login_fragment, container, false)
        val animateClass = CustomAnimate()

        val clickListenerButtonLog = View.OnClickListener {
            Toast.makeText(context,"222",Toast.LENGTH_SHORT).show()
            viewModel.signUpFirebase(
                UserData(
                    name = nameEdit.text.toString(),
                    pass = passEdit.text.toString(),
                    token = "",
                    signIn = true,
                    session = ""
                )
            )
            animateClass.alphaFadeOut(registerCard)
            animateClass.alphaFadeOut(buttonLog)
            buttonLog.isClickable = false
        }

        val buttonLog = v.findViewById<Button>(R.id.buttonLog)
        buttonLog.setOnClickListener(clickListenerButtonLog)
        buttonLog.isClickable = true

        viewModel.getUser().observe(viewLifecycleOwner, Observer {user ->
                callbackToActivity.userLogin(user)
        })

        viewModel.getStatus().observe(viewLifecycleOwner, Observer {msg ->

            if(msg != null) {
                animateClass.alphaFadeIn(layLoading)
                animateClass.alphaFadeOut(registerCard)
                animateClass.alphaFadeOut(buttonLog)
                textLoading.text = msg
            }else {
                animateClass.alphaFadeOut(layLoading)
            }
        })

        return v
    }
}
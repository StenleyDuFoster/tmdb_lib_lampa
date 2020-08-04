package lampa.test.tmdblib.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.login_fragment.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.fragments.callback.CallBackFromLoginFToActivity
import lampa.test.tmdblib.model.repository.data.UserData
import lampa.test.tmdblib.model.viewmodel.LoginViewModel

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

        val buttonLog = v.findViewById<Button>(R.id.buttonLog)
        buttonLog.setOnClickListener {
            viewModel.signUpFirebase(
                UserData(
                    name = nameEdit.text.toString(),
                    pass = passEdit.text.toString(),
                    token = "",
                    signIn = true,
                    session = ""
                )
            )
            buttonLog.isClickable = false
        }

        viewModel.getUser().observe(viewLifecycleOwner, Observer {user ->
                callbackToActivity.userLogin(user)
        })

        return v
    }
}
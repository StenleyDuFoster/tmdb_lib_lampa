package lampa.test.tmdblib.view.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.fragments.callback.CallBackFromLoginFToActivity
import lampa.test.tmdblib.model.repository.data.User
import lampa.test.tmdblib.model.viewmodel.LoginViewModel


class FragmentLogin : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var callbackToActivity: CallBackFromLoginFToActivity

    val auth = FirebaseAuth.getInstance()

    private lateinit var edit_name: EditText
    private lateinit var edit_pass: EditText
    private lateinit var button_log: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(activity!!).get(LoginViewModel::class.java)

        val v = inflater.inflate(R.layout.login_fragment, container, false)

        edit_name = v.findViewById(R.id.name_edit)
        edit_pass = v.findViewById(R.id.pass_edit)
        button_log = v.findViewById(R.id.b_log)

        button_log.setOnClickListener {

            //viewModel.createUser(name_edit.text.toString(), pass_edit.text.toString())
            viewModel.signUpFirebase(
                User(
                    name_edit.text.toString(),
                    pass_edit.text.toString(),
                    "",
                    true,
                    "")
            )
        }

        viewModel.getUser().observe(viewLifecycleOwner, Observer {user ->
                callbackToActivity.userLogin(user)
        })

        return v
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        callbackToActivity = activity as CallBackFromLoginFToActivity
    }
}
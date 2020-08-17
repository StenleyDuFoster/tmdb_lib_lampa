package lampa.test.tmdblib.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import kotlinx.android.synthetic.main.login_fragment.*

import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.LoginViewModel
import lampa.test.tmdblib.util.anim.CustomAnimate
import lampa.test.tmdblib.util.constant.FirebaseAuthConstant
import lampa.test.tmdblib.util.toast.makeToast
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.base.BaseFragment
import lampa.test.tmdblib.view.fragments.dialog.callback.CallBackFromDialogToFragment
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity
import lampa.test.tmdblib.view.fragments.dialog.FragmentPhoneAndCodeDialog


@Suppress("PLUGIN_WARNING")
class FragmentLogin : BaseFragment(R.layout.login_fragment),
    CallBackFromDialogToFragment {

    var alpha: Float = 1f
    lateinit var loginViewModel: LoginViewModel
    private lateinit var callbackToActivity: CallBackFromLoginFToActivity
    private var animateClass = CustomAnimate()
    private lateinit var fragmentPhoneDialog: FragmentPhoneAndCodeDialog

    private lateinit var verification_Id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        callbackToActivity = activity as CallBackFromLoginFToActivity
        view.setBackgroundColor(Color.WHITE)

        initButtonsLogView()
        initViewModel()
    }

    private fun initButtonsLogView(){

        val clickListenerButtonGoogle = View.OnClickListener {

            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(FirebaseAuthConstant().AUTH_GOOGLE_TOKEN)
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(activity!!, signInOptions)
            val signInIntent: Intent = signInClient.signInIntent
            startActivityForResult(signInIntent, 1)
            animateClass.alphaFadeIn(layLoading)
            google_button.isClickable = false
            phone_button.isClickable = false
            animateClass.alphaFadeOut(google_button)
            animateClass.alphaFadeOut(phone_button)
        }

        val clickListenerButtonPhone = View.OnClickListener {

            fragmentPhoneDialog = FragmentPhoneAndCodeDialog(R.integer.PHONE_DIALOG,this)
            fragmentPhoneDialog.show(activity!!.supportFragmentManager,null)
        }

        google_button.setOnClickListener(clickListenerButtonGoogle)
        phone_button.setOnClickListener(clickListenerButtonPhone)
    }

    private fun initViewModel(){

        loginViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            callbackToActivity.userLogin(user)
        })

        loginViewModel.getError().observe(viewLifecycleOwner, Observer { error ->

            google_button.isClickable = true
            animateClass.alphaFadeIn(google_button)
            phone_button.isClickable = true
            animateClass.alphaFadeIn(phone_button)
            view!!.setBackgroundColor(Color.WHITE)
            makeToast(error.message!!)
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
            view!!.setBackgroundColor(Color.TRANSPARENT)
            initAlphaView()
        })

        loginViewModel.getStatus().observe(viewLifecycleOwner, Observer { msg ->

            if(msg != null) {
                animateClass.alphaFadeIn(layLoading)
                textLoading.text = msg
            }else {
                animateClass.alphaFadeOut(layLoading)
            }
        })
    }

    private fun initAlphaView(){

        phone_button.alpha = alpha
        google_button.alpha = alpha
        mainLogoImage.alpha = alpha
    }

    override fun createCodeWithNumberPhone(number: String) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:$credential")

               // signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("TAG", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAG", "onCodeSent:$verificationId")
                verification_Id = verificationId

                // Save verification ID and resending token so we can use them later
//                storedVerificationId = verificationId
//                resendToken = token

                // ...
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+"+number, // Phone number to verify
            60, // Timeout duration
            java.util.concurrent.TimeUnit.SECONDS, // Unit of timeout
            activity!!, // Activity (for callback binding)
            callbacks)

        fragmentPhoneDialog = FragmentPhoneAndCodeDialog(R.integer.CODE_DIALOG,this)
        fragmentPhoneDialog.show(activity!!.supportFragmentManager,null)
    }

    override fun authWithCode(code: String) {

        animateClass.alphaFadeIn(layLoading)
        google_button.isClickable = false
        phone_button.isClickable = false
        animateClass.alphaFadeOut(google_button)
        animateClass.alphaFadeOut(phone_button)
        loginViewModel.firebaseSignInByPhone(verification_Id, code)

    }
}
package lampa.test.tmdblib.view.fragments.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.phone_and_code_dialog.view.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.view.fragments.dialog.callback.CallBackFromDialogToFragment

class FragmentPhoneAndCodeDialog(val type: Int,
                                 val callBackFromDialogToFragment: CallBackFromDialogToFragment): AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view: View = inflater.inflate(R.layout.phone_and_code_dialog, null)
        builder.setView(view)

        if (type == R.integer.PHONE_DIALOG){
            view.text_dialog.text = "Введите номер"
            view.save_button.setOnClickListener {
                callBackFromDialogToFragment.createCodeWithNumberPhone(view.edit_number.text.toString())
                this.dismiss()
            }
        }else{
            view.text_dialog.text = "Введите код"
            view.save_button.setOnClickListener {
                callBackFromDialogToFragment.authWithCode(view.edit_number.text.toString())
                this.dismiss()
            }
        }

        view.back_button.setOnClickListener {
            this.dismiss()
        }

        return builder.create()
    }
}
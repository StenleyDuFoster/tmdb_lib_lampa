package lampa.test.tmdblib.util.toast

import android.content.Context
import android.widget.Toast
import lampa.test.tmdblib.App

val context = App.contextComponent.getContext()

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun makeToast(message: String) {
    context.toast(message)
}
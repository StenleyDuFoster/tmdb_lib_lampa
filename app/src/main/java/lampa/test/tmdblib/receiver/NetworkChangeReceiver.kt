package lampa.test.tmdblib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import lampa.test.tmdblib.util.toast.makeToast

class NetworkChangeReceiver: BroadcastReceiver() {

    var doAfterConnectionWillResume:Runnable? = null

    companion object {
        var networkState = true
    }
    var oldNetworkState = true

    override fun onReceive(context: Context?, intent: Intent) {

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(cm.activeNetworkInfo == null)
        {
            networkState = false

            if(networkState != oldNetworkState)
                makeToast("соединение потеряно")

            oldNetworkState = networkState
        }
        else
        {
            networkState = true

            if(networkState != oldNetworkState)
                makeToast("соединение востановлено")

            oldNetworkState = networkState
            doAfterConnectionWillResume?.run()
        }
    }

    fun setRunnableCode(doAfterConnectionWillResume: Runnable){

        this.doAfterConnectionWillResume = doAfterConnectionWillResume
    }

    fun removeRunnableCode(){

        doAfterConnectionWillResume = null
    }
}
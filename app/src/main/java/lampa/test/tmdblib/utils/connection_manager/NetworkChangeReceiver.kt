package lampa.test.tmdblib.utils.connection_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast


class NetworkChangeReceiver: BroadcastReceiver() {

    var doAfterConnectionWillResume:Runnable? = null

    var oldNetworkState = true
    var newNetworkState = true

    override fun onReceive(context: Context?, intent: Intent) {

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(cm.activeNetworkInfo == null)
        {
            newNetworkState = false

            if(newNetworkState != oldNetworkState)
                Toast.makeText(context,"соединение потеряно", Toast.LENGTH_SHORT).show()

            oldNetworkState = newNetworkState
        }
        else
            {
                newNetworkState = true

                if(newNetworkState != oldNetworkState)
                    Toast.makeText(context,"соединение востановлено", Toast.LENGTH_SHORT).show()

                oldNetworkState = newNetworkState
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
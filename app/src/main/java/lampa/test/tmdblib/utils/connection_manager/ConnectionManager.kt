package lampa.test.tmdblib.utils.connection_manager

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.widget.Toast
import java.util.function.Consumer

class ConnectionManager (var context: Context, lambda: Runnable) {

    var checkLoopHandler: Boolean? = null
    var stateLoopHandler: Boolean? = null
    var lambda: Runnable

    init {
        this.lambda = lambda
    }

    fun checkInternet(): Boolean {
        val connection = hasConnection()
        if (!connection) {
            if (checkLoopHandler == null) waitConnection()
        }
        else{
            if (stateLoopHandler == null) stateConnection()
        }
        return connection
    }

    fun waitConnection() {
        checkLoopHandler = Handler().postDelayed({
            if (!hasConnection()) {
                waitConnection()
            } else {
                Toast.makeText(context, "Соединение установлено", Toast.LENGTH_SHORT).show()
                checkLoopHandler = null
                stateConnection()
                lambda.run()

            }
        }, 5000)
    }

    fun stateConnection(){
        stateLoopHandler = Handler().postDelayed({
            if (!hasConnection()) {
                waitConnection()
                Toast.makeText(context, "Соединение потеряно", Toast.LENGTH_SHORT).show()
                stateLoopHandler = null
            } else {
                stateConnection()
                lambda
            }
        }, 5000)
    }

    fun hasConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiInfo != null && wifiInfo.isConnected) {
            return true
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifiInfo != null && wifiInfo.isConnected) {
            return true
        }
        wifiInfo = cm.activeNetworkInfo
        return if (!(wifiInfo != null && wifiInfo.isConnected)) {
            false
        } else true
    }
}
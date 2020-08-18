package lampa.test.tmdblib.view.activity.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import lampa.test.tmdblib.R
import lampa.test.tmdblib.util.connection_manager.NetworkChangeReceiver

abstract class BaseActivity: AppCompatActivity() {

    lateinit var fragmentTransition:FragmentTransaction
    lateinit var networkChangeReceiver:NetworkChangeReceiver

    override fun onStart() {

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        networkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(networkChangeReceiver, intentFilter)
        super.onStart()
    }

    fun addWithBackStackFragmentToFragmentManager(fragment:Fragment, hideFragment:Fragment){

        initFragmentTransition()
        fragmentTransition.add(R.id.fragment_cont, fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.hide(hideFragment)
        fragmentTransition.commit()
    }

    fun addFragmentToFragmentManager(fragment:Fragment){

        initFragmentTransition()
        fragmentTransition.add(R.id.fragment_cont, fragment)
        fragmentTransition.commit()
    }

    fun initFragmentTransition(){

        fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.setCustomAnimations(
            R.anim.in_leaft_to_right, R.anim.out_leaft_to_right,
            R.anim.in_leaft_to_right, R.anim.out_leaft_to_right)
    }

    override fun onStop() {
        unregisterReceiver(networkChangeReceiver)
        super.onStop()
    }
}
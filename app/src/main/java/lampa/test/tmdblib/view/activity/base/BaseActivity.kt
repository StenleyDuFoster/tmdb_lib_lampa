package lampa.test.tmdblib.view.activity.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import lampa.test.tmdblib.R

abstract class BaseActivity: AppCompatActivity() {

    lateinit var fragmentTransition:FragmentTransaction

    fun addWithBackStackFragmentToFragmentManager(container_id:Int, fragment:Fragment){

        initFragmentTransition()
        fragmentTransition.add(container_id, fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }

    fun addFragmentToFragmentManager(container_id:Int, fragment:Fragment){

        initFragmentTransition()
        fragmentTransition.add(container_id, fragment)
        fragmentTransition.commit()
    }

    fun initFragmentTransition(){

        fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.setCustomAnimations(
            R.anim.in_leaft_to_right, R.anim.out_leaft_to_right,
            R.anim.in_leaft_to_right, R.anim.out_leaft_to_right)
    }
}
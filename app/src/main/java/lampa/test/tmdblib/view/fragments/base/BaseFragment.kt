package lampa.test.tmdblib.view.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import lampa.test.tmdblib.view.activity.base.BaseActivity

abstract class BaseFragment(val layoutId: Int): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onStop() {

        (activity!! as BaseActivity).networkChangeReceiver.removeRunnableCode()
        super.onStop()
    }
}
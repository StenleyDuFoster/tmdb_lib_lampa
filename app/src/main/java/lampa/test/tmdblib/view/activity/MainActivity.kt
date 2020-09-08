package lampa.test.tmdblib.view.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.FragmentDetails
import lampa.test.tmdblib.view.fragments.FragmentMain
import lampa.test.tmdblib.view.fragments.callback.CallBackFromMainFToActivity

class MainActivity : BaseActivity(), CallBackFromMainFToActivity {

    private var mainFragment: FragmentMain? = null
    private var detailsFragment: FragmentDetails? = null

    private lateinit var intentToLoginActivity: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        initMain()
    }

    private fun initMain() {

        intentToLoginActivity = Intent(this, LoginActivity::class.java)
        setActionBar(materialToolbar)
        title = "Фильмы"
        materialToolbar.inflateMenu(R.menu.main_toolbar)
        materialToolbar.setNavigationOnClickListener { onBackPressed() }
        materialToolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_out -> {

                    mainFragment?.movieViewModel?.logOut()
                    startActivity(intentToLoginActivity)
                    finish()

                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener true
            }
        }

        mainFragment = FragmentMain()
        mainFragment!!.session = intent.extras?.get("session_id") as String
        detailsFragment = FragmentDetails()
        addFragmentToFragmentManager(mainFragment!!)
    }

    override fun onBackPressed() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.title = "Фильмы"

        super.onBackPressed()
    }

    override fun openMovie(movie: MovieResultsTmdbData) {

        detailsFragment?.content = movie
        addWithBackStackFragmentToFragmentManager(detailsFragment!!, mainFragment!!)
    }
}
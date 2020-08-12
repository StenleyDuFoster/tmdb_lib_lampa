package lampa.test.tmdblib.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.utils.anim.CustomAnimate
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.FragmentDetails
import lampa.test.tmdblib.view.fragments.FragmentMain
import lampa.test.tmdblib.view.fragments.callback.CallBackFromMainFToActivity

class MainActivity : BaseActivity(), CallBackFromMainFToActivity {

    private var mainFragment: FragmentMain? = null
    private var detailsFragment: FragmentDetails? = null

    lateinit var intentToLoginActivity: Intent

    private val animateClass = CustomAnimate()

    private lateinit var searchTypeMovie: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        mainFragment = FragmentMain(intent.extras?.get("session_id") as String)
        detailsFragment = FragmentDetails()
        addFragmentToFragmentManager(R.id.fragment_cont_constrain, mainFragment!!)
        initButtonLayoutManager()
        initButtonMyLikeList()
        initSpinner()
    }

    private fun initButtonLayoutManager() {

        val clickListenerLayoutManager = View.OnClickListener{v ->
            when(v.id){
                R.id.buttonLinear ->
                {
                    animateClass.scale(buttonLinear,1.0f)
                    animateClass.scale(buttonGrid,0.6f)
                    mainFragment?.setLayoutManager(1)
                }
                R.id.buttonGrid ->
                {
                    animateClass.scale(buttonGrid,1.0f)
                    animateClass.scale(buttonLinear,0.6f)
                    mainFragment?.setLayoutManager(2)
                }
            }
        }
        buttonLinear.setOnClickListener(clickListenerLayoutManager)
        buttonGrid.setOnClickListener(clickListenerLayoutManager)
    }

    private fun initButtonMyLikeList(){

        val likeFragment = FragmentMain(intent.extras?.get("session_id") as String)

        buttonMyLikeList.setOnClickListener {

            addWithBackStackFragmentToFragmentManager(R.id.fragment_details_constrain, likeFragment!!)
            likeFragment?.getMyLikeList()
        }
    }

    private fun initSpinner(){

        val spinnerItems:Array<String> = arrayOf(
            getString(R.string.search_now_playing),
            getString(R.string.search_popular),
            getString(R.string.search_top_rated),
            getString(R.string.search_upcoming)
        )
        val spinnerUserVisible:Array<String> = arrayOf(
            "сейчас в прокате",
            "популярные",
            "лучшие оценки",
            "скоро выйдут"
        )
        val arraySpinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerUserVisible)
        arraySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arraySpinnerAdapter

        val spinnerItemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                  searchTypeMovie = spinnerItems[position]

                  if(mainFragment != null)
                    mainFragment?.changeMovieTypeFromFragment(spinnerItems[position])
              }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner.onItemSelectedListener = spinnerItemSelectedListener
    }

    override fun onBackPressed() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.title = "Фильмы"

        super.onBackPressed()
    }

    override fun openMovie(movie: MovieResultsTmdbData) {

        detailsFragment?.content = movie
        addWithBackStackFragmentToFragmentManager(R.id.fragment_details_constrain, detailsFragment!!)
    }
}
package lampa.test.tmdblib.view.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import lampa.test.tmdblib.R
import lampa.test.tmdblib.view.fragments.FragmentDetails
import lampa.test.tmdblib.view.fragments.FragmentMain
import lampa.test.tmdblib.view.fragments.callback.CallBackFromLoginFToActivity
import lampa.test.tmdblib.view.fragments.callback.CallBackFromMainFToActivity
import lampa.test.tmdblib.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.repository.data.UserData
import lampa.test.tmdblib.utils.anim.CustomAnimate
import lampa.test.tmdblib.view.fragments.FragmentLogin

class MainActivity : AppCompatActivity(), CallBackFromMainFToActivity,
    CallBackFromLoginFToActivity {

    private var mainFragment: FragmentMain? = null
    private val detailsFragment = FragmentDetails()
    private val loginFragment = FragmentLogin()

    private var fTrans = supportFragmentManager.beginTransaction()

    private var page:Int = 1
    private var totalPage:Int? = null

    private val animateClass = CustomAnimate()

    private lateinit var searchTypeMovie: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMain()
    }

    private fun initMain(){

        setActionBar(materialToolbar)
        actionBar?.title = "Фильмы"
        materialToolbar.setNavigationOnClickListener { onBackPressed() }

        fTrans = supportFragmentManager.beginTransaction()

        fTrans.add(R.id.fragment_details_constrain, detailsFragment)
        fTrans.add(R.id.fragment_details_constrain, loginFragment)
        fTrans.hide(detailsFragment)

        fTrans.commit()
    }

    private fun initButtonLayoutManager() {

        val clickListenerLayoutManager = View.OnClickListener{v ->
            fTrans = supportFragmentManager.beginTransaction()
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

    private fun initButtonNextBack(){

        val clickListenerPageManager = View.OnClickListener{v ->
            fTrans = supportFragmentManager.beginTransaction()
            when(v.id){
                R.id.buttonNextPage ->
                {
                    if(page < totalPage!!) {
                        page ++
                        mainFragment?.getPage()
                        buttonBackPage.alpha = 1.0f
                    }
                    if(page == totalPage){
                        buttonNextPage.alpha = 0.5f
                    }
                }
                R.id.buttonBackPage ->
                {
                    if(page > 1) {
                        page --
                        mainFragment?.getPage()
                        buttonNextPage.alpha = 1.0f
                    }
                    if(page == 1){
                        buttonBackPage.alpha = 0.5f
                    }
                }
            }
        }
        buttonNextPage.setOnClickListener(clickListenerPageManager)
        buttonBackPage.setOnClickListener(clickListenerPageManager)
    }

    private fun initButtonMyLikeList(){

        buttonMyLikeList.setOnClickListener {

            mainFragment?.getMyLikeList()
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
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerUserVisible)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val itemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                  searchTypeMovie = spinnerItems[position]
                  page = 1

                  if(mainFragment != null)
                    mainFragment?.changeMovieTypeFromFragment(spinnerItems[position])
              }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner.onItemSelectedListener = itemSelectedListener
    }

    override fun onBackPressed() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.title = "Фильмы"
        super.onBackPressed()
    }

    private fun initFTrans(){

        fTrans = supportFragmentManager.beginTransaction()
        fTrans.setCustomAnimations(R.anim.in_leaft_to_right, R.anim.out_leaft_to_right,
            R.anim.in_leaft_to_right, R.anim.out_leaft_to_right)
    }

    override fun openMovie(movie: MovieResultsTmdbData) {

        initFTrans()
        detailsFragment.setContent(movie)

        fTrans.show(detailsFragment)
        fTrans.addToBackStack(null)
        fTrans.commit()
    }

    override fun userLogin(userData: UserData) {

        initFTrans()
        mainFragment = FragmentMain(userData.session)

        fTrans.add(R.id.fragment_cont_constrain, mainFragment!!)
        fTrans.hide(loginFragment)

        fTrans.commit()
        initSpinner()
        initButtonMyLikeList()
        initButtonLayoutManager()
        initButtonNextBack()
    }
}
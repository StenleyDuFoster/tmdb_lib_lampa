package lampa.test.tmdblib.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import lampa.test.tmdblib.R
import lampa.test.tmdblib.fragments.FragmentDetails
import lampa.test.tmdblib.fragments.FragmentMain
import lampa.test.tmdblib.fragments.callback.CallBackFromFragmentToActivity
import lampa.test.tmdblib.model.data.Results
import lampa.test.tmdblib.utils.anim.Animate

class MainActivity : AppCompatActivity(), CallBackFromFragmentToActivity{

    private val mainFragment = FragmentMain()
    private val detailsFragment = FragmentDetails()

    private var fTrans = supportFragmentManager.beginTransaction()

    private var page:Int = 1
    private var totalPage:Int? = null

    private val animateClass = Animate()

    private lateinit var searchTypeMovie: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Init()
    }

    private fun Init(){

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setActionBar(toolbar)
        actionBar?.title = "Фильмы"
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fTrans.add(R.id.fragment_cont_constrain, mainFragment)
        fTrans.add(R.id.fragment_details_constrain, detailsFragment)
        fTrans.hide(detailsFragment)
        fTrans.commit()

        initButtonLayoutManager()
        initButtonNextBack()
        initSpinner()
    }

    private fun initButtonLayoutManager() {

        val b_linear = findViewById<Button>(R.id.b_linear)
        val b_grid = findViewById<Button>(R.id.b_grid)

        val clickListenerLayoutManager = View.OnClickListener{v ->
            fTrans = supportFragmentManager.beginTransaction()
            when(v.id){
                R.id.b_linear ->
                {
                    animateClass.scale(b_linear,1.0f)
                    animateClass.scale(b_grid,0.6f)
                    mainFragment.setLayoutManager(1)
                }
                R.id.b_grid ->
                {
                    animateClass.scale(b_grid,1.0f)
                    animateClass.scale(b_linear,0.6f)
                    mainFragment.setLayoutManager(2)
                }
            }
        }
        b_linear.setOnClickListener(clickListenerLayoutManager)
        b_grid.setOnClickListener(clickListenerLayoutManager)
    }

    private fun initButtonNextBack(){

        val b_next_page = findViewById<Button>(R.id.b_next_page)
        val b_back_page = findViewById<Button>(R.id.b_back_page)

        val clickListenerPageManager = View.OnClickListener{v ->
            fTrans = supportFragmentManager.beginTransaction()
            when(v.id){
                R.id.b_next_page ->
                {
                    if(page < totalPage!!) {
                        page ++
                        mainFragment.getPage()
                        b_back_page.alpha = 1.0f
                    }
                    if(page == totalPage){
                        b_next_page.alpha = 0.5f
                    }
                }
                R.id.b_back_page ->
                {
                    if(page > 1) {
                        page --
                        mainFragment.getPage()
                        b_next_page.alpha = 1.0f
                    }
                    if(page == 1){
                        b_back_page.alpha = 0.5f
                    }
                }
            }
        }
        b_next_page.setOnClickListener(clickListenerPageManager)
        b_back_page.setOnClickListener(clickListenerPageManager)
    }

    private fun initSpinner(){

        val spinner_items:Array<String> = arrayOf(
            getString(R.string.search_now_playing),
            getString(R.string.search_popular),
            getString(R.string.search_top_rated),
            getString(R.string.search_upcoming)
        )
        val spinner_user_visible:Array<String> = arrayOf(
            "сейчас в прокате",
            "популярные",
            "лучшие оценки",
            "скоро выйдут"
        )

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinner_user_visible)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val itemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                  searchTypeMovie = spinner_items[position]
                  page = 1
                  mainFragment.changeMovieTypeFromFragment(spinner_items[position])
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun openMovie(movie: Results) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        detailsFragment.setContent(movie)
        fragmentTransaction.setCustomAnimations(R.anim.in_leaft_to_right, R.anim.out_leaft_to_right,
                                                R.anim.in_leaft_to_right, R.anim.out_leaft_to_right)

        fragmentTransaction.show(detailsFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
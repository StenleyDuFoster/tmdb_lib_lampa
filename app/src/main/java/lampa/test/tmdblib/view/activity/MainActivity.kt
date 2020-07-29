package lampa.test.tmdblib.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import lampa.test.tmdblib.R
import lampa.test.tmdblib.fragments.FragmentDetails
import lampa.test.tmdblib.fragments.FragmentMain
import lampa.test.tmdblib.contract_interface.MainContract.Presenter
import lampa.test.tmdblib.fragments.callback.CallBackFromFragmentToActivity
import lampa.test.tmdblib.model.data.Results
import lampa.test.tmdblib.view.anim.Animate


class MainActivity : AppCompatActivity(),CallBackFromFragmentToActivity{


    var mPresenter: Presenter? = null

    val mainFragment = FragmentMain()
    val detailsFragment = FragmentDetails()

    var fTrans = supportFragmentManager.beginTransaction()

    var page:Int = 1
    var totalPage:Int? = null

    lateinit var searchTypeMovie: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Init()
    }

    private fun Init(){

        val b_linear = findViewById<Button>(R.id.b_linear)
        val b_grid = findViewById<Button>(R.id.b_grid)
        val b_next_page = findViewById<Button>(R.id.b_next_page)
        val b_back_page = findViewById<Button>(R.id.b_back_page)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setActionBar(toolbar)
        actionBar?.setTitle("Фильмы")
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fTrans.add(R.id.fragment_cont_constrain, mainFragment)
        fTrans.add(R.id.fragment_details_constrain, detailsFragment)
        fTrans.hide(detailsFragment)
        fTrans.commit()

        val clickListenerLayoutManager = View.OnClickListener{v ->
            fTrans = supportFragmentManager.beginTransaction()
            when(v.id){
                R.id.b_linear ->
                {
                    Animate().scale(b_linear,1.0f)
                    Animate().scale(b_grid,0.6f)
                    mainFragment.setLayoutManager(1)
                }
                R.id.b_grid ->
                {
                    Animate().scale(b_grid,1.0f)
                    Animate().scale(b_linear,0.6f)
                    mainFragment.setLayoutManager(2)
                }
            }
        }
        b_linear.setOnClickListener(clickListenerLayoutManager)
        b_grid.setOnClickListener(clickListenerLayoutManager)

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
                view: View,
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
        spinner.setSelection(1)
    }

    override fun onBackPressed() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)
        actionBar?.setTitle("Фильмы")
        super.onBackPressed()
    }

    override fun openMovie(movie: Results) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        detailsFragment.setContent(movie)

        fragmentTransaction.show(detailsFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
package lampa.test.tmdblib

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

import lampa.test.tmdblib.api.JsonPlaceHolderApi
import lampa.test.tmdblib.api.Movie
import lampa.test.tmdblib.api.Results
import lampa.test.tmdblib.fragments.FragmentDetails
import lampa.test.tmdblib.fragments.FragmentMain
import lampa.test.tmdblib.fragments.callback.CallBackFromFragmentToActivity
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), CallBackFromFragmentToActivity{

    val mainFragment = FragmentMain()
    val detailsFragment = FragmentDetails()

    var fTrans = supportFragmentManager.beginTransaction()

    lateinit var postMovie: Movie

    var page:Int = 1
    var totalPage:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Init()
        retrofitDownload(page, R.integer.ALL_PAGE)
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
                    scaleAnimate(b_linear,1.0f)
                    scaleAnimate(b_grid,0.6f)
                    mainFragment.setLayoutManager(1)
                }
                R.id.b_grid ->
                {
                    scaleAnimate(b_grid,1.0f)
                    scaleAnimate(b_linear,0.6f)
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
                        retrofitDownload(page, R.integer.ALL_PAGE)
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
                        retrofitDownload(page, R.integer.ALL_PAGE)
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

    private fun retrofitDownload(page_r:Int, downloadTo:Int){

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        val call: Call<Movie>? = jsonPlaceHolderApi.getPostsMovie("ru", page_r)

        call?.enqueue(object : Callback<Movie?> {
            override fun onResponse(
                call: Call<Movie?>,
                response: Response<Movie?>
            ) {
                postMovie = response.body()!!

                when(downloadTo){
                    R.integer.ALL_PAGE -> {
                        mainFragment.setContent(postMovie?.results)
                    }
                    R.integer.ADD_TO_PAGE -> {
                        mainFragment.addContent(postMovie?.results)
                        mainFragment.isDownload = false
                    }

                }

                if(totalPage == null)
                    totalPage = postMovie?.total_pages
            }

            override fun onFailure(
                call: Call<Movie?>,
                t: Throwable?
            ) {
            }
        })
    }

    fun scaleAnimate(v:View, final:Float){
        var animX = ObjectAnimator.ofFloat(v,View.SCALE_X,v.scaleX,final)
        var animY = ObjectAnimator.ofFloat(v,View.SCALE_Y,v.scaleY,final)
        var anim = AnimatorSet()
        anim.setDuration(300)
        anim.play(animX).with(animY)
        anim.start()
    }

    override fun openMovie(movie: Results) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        detailsFragment.setContent(movie)

        fragmentTransaction.show(detailsFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun addMovieToList() {
        page ++
        retrofitDownload(page, R.integer.ADD_TO_PAGE)
    }

    override fun onBackPressed() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)
        actionBar?.setTitle("Фильмы")
        super.onBackPressed()
    }
}
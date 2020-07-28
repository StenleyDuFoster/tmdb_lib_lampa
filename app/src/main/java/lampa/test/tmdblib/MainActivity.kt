package lampa.test.tmdblib

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.AsyncTask
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
        RetrofitDownload(page, R.integer.ALL_PAGE).execute()
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
                        RetrofitDownload(page, R.integer.ALL_PAGE).execute()
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
                        RetrofitDownload(page, R.integer.ALL_PAGE).execute()
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

    inner class RetrofitDownload (page_r:Int, downloadTo:Int): AsyncTask<Void, Void, Void>() {

        val page_r:Int
        val downloadTo:Int

        init {
            this.page_r = page_r
            this.downloadTo = downloadTo
        }

        override fun doInBackground(vararg p0: Void?): Void? {
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
                            mainFragment.AddContent(postMovie?.results).execute()
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
            return null
        }
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

        if(page < this!!.totalPage!!)
            page ++

        RetrofitDownload(page, R.integer.ADD_TO_PAGE).execute()
    }

    override fun onBackPressed() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)
        actionBar?.setTitle("Фильмы")
        super.onBackPressed()
    }
}
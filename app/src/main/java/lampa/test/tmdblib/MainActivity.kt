package lampa.test.tmdblib

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import lampa.test.tmdblib.api.JsonPlaceHolderApi
import lampa.test.tmdblib.api.Movie
import lampa.test.tmdblib.fragments.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val linearFragment = Fragment(1)
    val gridFragment = Fragment(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Init()
        retrofitDownload(2)
    }

    fun Init(){

        val b_linear = findViewById<Button>(R.id.b_linear)
        val b_grid = findViewById<Button>(R.id.b_grid)
        var fTrans = supportFragmentManager.beginTransaction()
        fTrans.add(R.id.fragment_cont_constrain, linearFragment)
        fTrans.add(R.id.fragment_cont_constrain, gridFragment)
        fTrans.hide(gridFragment)
//        fTrans.hide(linearFragment)
        fTrans.commit()

        val clickListener = View.OnClickListener{v ->
            fTrans = supportFragmentManager.beginTransaction()
            when(v.id){
                R.id.b_linear ->
                {
                    scaleAnimate(b_linear,1.0f)
                    scaleAnimate(b_grid,0.8f)
                    fTrans.hide(gridFragment)
                    fTrans.show(linearFragment)
                }
                R.id.b_grid ->
                {
                    scaleAnimate(b_grid,1.0f)
                    scaleAnimate(b_linear,0.8f)
                    fTrans.hide(linearFragment)
                    fTrans.show(gridFragment)
                }
            }
            fTrans.commit()
        }
        b_linear.setOnClickListener(clickListener)
        b_grid.setOnClickListener(clickListener)
    }

    fun retrofitDownload(page:Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        val call: Call<Movie>? = jsonPlaceHolderApi.getPostsMovie("ru",page)

        call?.enqueue(object : Callback<Movie?> {
            override fun onResponse(
                call: Call<Movie?>,
                response: Response<Movie?>
            ) {
                if (!response.isSuccessful()) {
                }
                val postMovie: Movie? = response.body()
                gridFragment.setContent(postMovie?.results)
                linearFragment.setContent(postMovie?.results)
                Log.v("200",postMovie.toString())
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
}
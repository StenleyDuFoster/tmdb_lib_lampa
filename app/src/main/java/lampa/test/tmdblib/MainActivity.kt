package lampa.test.tmdblib

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import lampa.test.tmdblib.api.JsonPlaceHolderApi
import lampa.test.tmdblib.api.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRetrofit()
    }

    fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/trending/all/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        val call: Call<Movie>? = jsonPlaceHolderApi.getPostsMovie()

        call?.enqueue(object : Callback<Movie?> {
            override fun onResponse(
                call: Call<Movie?>,
                response: Response<Movie?>
            ) {
                if (!response.isSuccessful()) {
                }
                val postMovie: Movie? = response.body()
                val text:TextView = findViewById(R.id.text_main)
                text.setText(postMovie.toString())
            }

            override fun onFailure(
                call: Call<Movie?>,
                t: Throwable?
            ) {
            }
        })
    }
}
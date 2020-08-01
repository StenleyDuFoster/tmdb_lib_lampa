package lampa.test.tmdblib.model.repository.internet

import android.os.AsyncTask
import lampa.test.tmdblib.BuildConfig
import lampa.test.tmdblib.contract_interface.CallBackFromRepositoryToViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.internet.api.JsonPlaceHolderApi
import lampa.test.tmdblib.model.repository.data.Movie

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InternetRepository(callBackFromRepositoryToMainContract: CallBackFromRepositoryToViewModel)
    : MainContract.Repository {

    private lateinit var postMovie: Movie
    private var page: Int = 1
    private var addPage: Int = 0
    private var totalPage: Int = 1
    private var searchTypeMovie: String = "popular"
    private val callBackFromRepositoryToMainContract:CallBackFromRepositoryToViewModel

    private val interceptor:HttpLoggingInterceptor

    private val client:OkHttpClient

    private val retrofit:Retrofit
    private val jsonPlaceHolderApi:JsonPlaceHolderApi

    init{
        this.callBackFromRepositoryToMainContract = callBackFromRepositoryToMainContract

        interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE

        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
    }

    override fun setMovieType(movieType: String){
        searchTypeMovie = movieType
        loadPageMovie()
    }

    private inner class LoadMovie: AsyncTask<Void,Void,Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {

            val call: Call<Movie>? = jsonPlaceHolderApi
                .getTopRatedMovie(searchTypeMovie,
                    "9bb79091064ef827e213e1b974a3b718",
                    "ru",
                    page)

            call?.enqueue(object : Callback<Movie?> {
                override fun onResponse(
                    call: Call<Movie?>,
                    response: Response<Movie?>
                ) {
                    postMovie = response.body()!!

                    callBackFromRepositoryToMainContract.onMovieLoad(postMovie)

                    page += addPage

                    if(totalPage == null)
                        totalPage = postMovie?.total_pages
                }

                override fun onFailure(
                    call: Call<Movie?>,
                    t: Throwable?
                ) {
                    callBackFromRepositoryToMainContract.onFailure(t.toString())
                }
            })
            return null
        }
    }

    override fun loadPageMovie() {

        page = 1
        LoadMovie().execute()
    }

    override fun loadAddPageMovie() {

        page += 1
        LoadMovie().execute()
    }
}
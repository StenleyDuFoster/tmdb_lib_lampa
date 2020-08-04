package lampa.test.tmdblib.model.repository.internet

import android.os.AsyncTask
import android.util.Log
import lampa.test.tmdblib.BuildConfig
import lampa.test.tmdblib.contract_interface.CallBackFromInternetMovieToMovieViewModel
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

class InternetMovieLoader(callBackFromInternetMovieToMovieViewModel: CallBackFromInternetMovieToMovieViewModel)
    : MainContract.InternetLoadMovie {

    private lateinit var postMovie: Movie
    private var page: Int = 1
    private var totalPage: Int = 1
    private var searchTypeMovie: String = "popular"
    private var ADD_TO_FAVORITE = false
    private lateinit var session_id: String
    private val callBackFromInternetMovieToMovieMainContract:CallBackFromInternetMovieToMovieViewModel

    private val interceptor:HttpLoggingInterceptor
    private val client:OkHttpClient

    private lateinit var retrofit:Retrofit
    private lateinit var jsonPlaceHolderApi:JsonPlaceHolderApi

    init{
        this.callBackFromInternetMovieToMovieMainContract = callBackFromInternetMovieToMovieViewModel

        interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE

        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    override fun setMovieType(movieType: String){
        searchTypeMovie = movieType
        loadPageMovie()
    }

    private inner class LoadListMovie: AsyncTask<Void,Void,Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {

            retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

            val call: Call<Movie>? = jsonPlaceHolderApi
                .getListMovie(searchTypeMovie,
                    "9bb79091064ef827e213e1b974a3b718",
                    "ru",
                    page)

            call?.enqueue(object : Callback<Movie?> {
                override fun onResponse(
                    call: Call<Movie?>,
                    response: Response<Movie?>
                ) {
                    postMovie = response.body()!!

                    callBackFromInternetMovieToMovieMainContract.onMovieLoad(postMovie)

                    if(totalPage == null)
                        totalPage = postMovie?.total_pages
                }

                override fun onFailure(
                    call: Call<Movie?>,
                    t: Throwable?
                ) {
                    callBackFromInternetMovieToMovieMainContract.onFailure(t.toString())
                }
            })
            return null
        }
    }

    private inner class LoadLikeMovie: AsyncTask<Void,Void,Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {

            retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/guest_session/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

            val call: Call<Movie>? = jsonPlaceHolderApi
                .getLikeMovie(session_id,
                    "9bb79091064ef827e213e1b974a3b718",
                    "ru",
                    "created_at.asc")

            call?.enqueue(object : Callback<Movie?> {
                override fun onResponse(
                    call: Call<Movie?>,
                    response: Response<Movie?>
                ) {
                    postMovie = response.body()!!

                    callBackFromInternetMovieToMovieMainContract.onMovieLoad(postMovie)

                    if(totalPage == null)
                        totalPage = postMovie?.total_pages
                }

                override fun onFailure(
                    call: Call<Movie?>,
                    t: Throwable?
                ) {
                    callBackFromInternetMovieToMovieMainContract.onFailure(t.toString())
                }
            })
            return null
        }
    }

    override fun loadPageMovie() {

        ADD_TO_FAVORITE = false
        page = 1
        LoadListMovie().execute()
    }

    override fun loadAddPageMovie() {

        page += 1
        if(!ADD_TO_FAVORITE)
            LoadListMovie().execute()
    }

    override fun loadLikeListMovie(session_id: String) {

        ADD_TO_FAVORITE = true
        this.session_id = session_id
        LoadLikeMovie().execute()
    }
}
package lampa.test.tmdblib.repository.internet

import android.os.AsyncTask
import lampa.test.tmdblib.BuildConfig
import lampa.test.tmdblib.contract_interface.CallBackFromInternetMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.repository.internet.api.JsonPlaceHolderApi
import lampa.test.tmdblib.repository.data.MovieTmdbData
import lampa.test.tmdblib.repository.data.WrapperMovieData

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InternetMovieLoader(val callBackFromInternetMovieToMovieViewModel: CallBackFromInternetMovieToMovieViewModel)
    : MainContract.InternetLoadMovie {

    private lateinit var postMovieTmdbData: MovieTmdbData
    private var page: Int = 1
    private var totalPage: Int = 1
    private var searchTypeMovie: String = "popular"
    private var ADD_TO_FAVORITE = false
    private lateinit var session_id: String

    private val interceptor:HttpLoggingInterceptor
    private val client:OkHttpClient

    private lateinit var retrofit:Retrofit
    private lateinit var jsonPlaceHolderApi:JsonPlaceHolderApi

    init{

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

            val call: Call<MovieTmdbData>? = jsonPlaceHolderApi
                .getListMovie(searchTypeMovie,
                    "9bb79091064ef827e213e1b974a3b718",
                    "ru",
                    page)

            call?.enqueue(object : Callback<MovieTmdbData?> {
                override fun onResponse(
                    call: Call<MovieTmdbData?>,
                    response: Response<MovieTmdbData?>
                ) {
                    postMovieTmdbData = response.body()!!

                    callBackFromInternetMovieToMovieViewModel.onMovieLoad(
                        WrapperMovieData(
                        0,postMovieTmdbData,ADD_TO_FAVORITE)
                    )

                }

                override fun onFailure(
                    call: Call<MovieTmdbData?>,
                    t: Throwable?
                ) {
                    callBackFromInternetMovieToMovieViewModel.onFailure(t.toString())
                }
            })
            return null
        }
    }

    private inner class LoadLikeMovie: AsyncTask<Void,Void,Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {

            retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

            val call: Call<MovieTmdbData>? = jsonPlaceHolderApi
                .getLikeMovie(session_id,
                    "9bb79091064ef827e213e1b974a3b718",
                    "ru",
                    page,
                    "created_at.asc")

            call?.enqueue(object : Callback<MovieTmdbData?> {
                override fun onResponse(
                    call: Call<MovieTmdbData?>,
                    response: Response<MovieTmdbData?>
                ) {
                    postMovieTmdbData = response.body()!!
                    var wrapperMovie = WrapperMovieData(0,postMovieTmdbData,ADD_TO_FAVORITE)
                    callBackFromInternetMovieToMovieViewModel.onMovieLoad(wrapperMovie)

                }

                override fun onFailure(
                    call: Call<MovieTmdbData?>,
                    t: Throwable?
                ) {
                    callBackFromInternetMovieToMovieViewModel.onFailure(t.toString())
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
        if(totalPage <= page) {

            if (!ADD_TO_FAVORITE)
                LoadListMovie().execute()
            else
                LoadLikeMovie().execute()
        }
    }

    override fun loadLikeListMovie(session_id: String) {

        page = 1
        ADD_TO_FAVORITE = true
        this.session_id = session_id
        LoadLikeMovie().execute()
    }
}
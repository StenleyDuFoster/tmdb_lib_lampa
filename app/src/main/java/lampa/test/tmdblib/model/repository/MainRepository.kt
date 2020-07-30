package lampa.test.tmdblib.model.repository

import lampa.test.tmdblib.BuildConfig
import lampa.test.tmdblib.contract_interface.CallBackFromRepositoryToViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.api.JsonPlaceHolderApi
import lampa.test.tmdblib.model.data.Movie

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository(callBackFromRepositoryToMainContract: CallBackFromRepositoryToViewModel)
    : MainContract.Repository {

    lateinit var postMovie: Movie
    var page: Int = 1
    var totalPage: Int = 1
    var searchTypeMovie: String = "popular"
    val callBackFromRepositoryToMainContract:CallBackFromRepositoryToViewModel

    val interceptor:HttpLoggingInterceptor

    val client:OkHttpClient

    val retrofit:Retrofit
    val jsonPlaceHolderApi:JsonPlaceHolderApi

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
    }

    override fun loadMovie() {

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
}
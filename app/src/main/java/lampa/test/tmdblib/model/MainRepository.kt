package lampa.test.tmdblib.model

import lampa.test.tmdblib.api.JsonPlaceHolderApi
import lampa.test.tmdblib.contract_interface.CallBackFromRepositoryToMainContract
import lampa.test.tmdblib.model.data.Movie
import lampa.test.tmdblib.contract_interface.MainContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository(callBackFromRepositoryToMainContract: CallBackFromRepositoryToMainContract)
    : MainContract.Repository {

    lateinit var postMovie: Movie
    var page: Int = 1
    var totalPage: Int = 1
    var searchTypeMovie: String = "top_rated"
    val callBackFromRepositoryToMainContract:CallBackFromRepositoryToMainContract

    init{
        this.callBackFromRepositoryToMainContract = callBackFromRepositoryToMainContract
    }

    override fun setMovieType(movieType: String){
        searchTypeMovie = movieType
    }

    override fun loadMovie() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

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
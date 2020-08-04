package lampa.test.tmdblib.model.repository.internet

import android.os.AsyncTask
import lampa.test.tmdblib.BuildConfig
import lampa.test.tmdblib.contract_interface.CallBackFromInternetPostMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.data.PostMovieRatingData
import lampa.test.tmdblib.model.repository.data.PostResponseData
import lampa.test.tmdblib.model.repository.internet.api.JsonPlaceHolderApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InternetPostRateMovie(callBackFromInternetPostMovieToMovieViewModel: CallBackFromInternetPostMovieToMovieViewModel)
    : MainContract.InternetPostLikeMovie {

    val callBackFromInternetPostMovieToMovieViewModel:CallBackFromInternetPostMovieToMovieViewModel
    val retrofit:Retrofit
    val jsonPlaceHolderApi:JsonPlaceHolderApi

    init{
        this.callBackFromInternetPostMovieToMovieViewModel = callBackFromInternetPostMovieToMovieViewModel

        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
    }

    override fun postAddToLike(session_id:String, movie_id: Int) {

        postLikeToTMDB(session_id, movie_id).execute()
    }

    override fun postDeleteLike(session_id:String, movie_id: Int) {

        postDeleteToTMDB(session_id, movie_id).execute()
    }

    private inner class postLikeToTMDB(session_id:String, movie_id: Int): AsyncTask<Void, Void, Void>(){

        val session_id = session_id
        val movie_id = movie_id

        override fun doInBackground(vararg p0: Void?): Void? {

            val call: Call<PostResponseData> = jsonPlaceHolderApi
                .postLikeMovie(movie_id,"9bb79091064ef827e213e1b974a3b718",
                    session_id, PostMovieRatingData(8f))

            call?.enqueue(object : Callback<PostResponseData?> {
                override fun onResponse(
                    call: Call<PostResponseData?>,
                    responseData: Response<PostResponseData?>
                ) {
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(responseData.body()?.status_message.toString())
                }

                override fun onFailure(
                    call: Call<PostResponseData?>,
                    t: Throwable?
                ) {
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(t.toString())
                }
            })
            return null
        }
    }

    private inner class postDeleteToTMDB(session_id:String, movie_id: Int): AsyncTask<Void, Void, Void>(){

        val session_id = session_id
        val movie_id = movie_id

        override fun doInBackground(vararg p0: Void?): Void? {

            val call: Call<PostResponseData> = jsonPlaceHolderApi
                .deleteLikeMovie(movie_id,"9bb79091064ef827e213e1b974a3b718",
                    session_id)

            call?.enqueue(object : Callback<PostResponseData?> {
                override fun onResponse(
                    call: Call<PostResponseData?>,
                    responseData: Response<PostResponseData?>
                ) {
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(responseData.body()?.status_message.toString())
                }

                override fun onFailure(
                    call: Call<PostResponseData?>,
                    t: Throwable?
                ) {
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(t.toString())
                }
            })
            return null
        }
    }
}
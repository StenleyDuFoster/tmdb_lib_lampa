package lampa.test.tmdblib.repository.internet

import android.os.AsyncTask
import lampa.test.tmdblib.BuildConfig
import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.repository.data.SessionTmdbData
import lampa.test.tmdblib.repository.internet.api.JsonPlaceHolderApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InternetAuthenticationTmdb (val callBackFromInternetAuthToLoginViewModel: CallBackFromInternetAuthToLoginViewModel)
    : MainContract.InternetAuth {

    private val retrofit: Retrofit
    val jsonPlaceHolderApi: JsonPlaceHolderApi

    init{

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
            .baseUrl("https://api.themoviedb.org/3/authentication/guest_session/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
        createSession()
    }

    private inner class LoadSession: AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {

            val call: Call<SessionTmdbData>? = jsonPlaceHolderApi
                .getSession("9bb79091064ef827e213e1b974a3b718")

            call?.enqueue(object : Callback<SessionTmdbData?> {
                override fun onResponse(
                    call: Call<SessionTmdbData?>,
                    response: Response<SessionTmdbData?>
                ) {
                    callBackFromInternetAuthToLoginViewModel.onAuthenticationTmdbSuccess(response.body()?.guest_session_id!!)
                }

                override fun onFailure(
                    call: Call<SessionTmdbData?>,
                    t: Throwable?
                ) {
                    //callBackFromInternetAuthToLoginViewModel.onFailure(t.toString())
                }
            })
            return null
        }
    }

    override fun createSession() {

        LoadSession().execute()
    }
}
package lampa.test.tmdblib.dagger.module

import dagger.Module
import dagger.Provides

import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.repository.internet.api.JsonTmdbPlaceHolderApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitTmdbModule {

    @Provides
    @Singleton
    fun getPlaceHolder(): JsonTmdbPlaceHolderApi {
        var interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory
                    .createWithScheduler(Schedulers.io()))
            .client(client)
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonTmdbPlaceHolderApi::class.java)
        return jsonPlaceHolderApi
    }
}
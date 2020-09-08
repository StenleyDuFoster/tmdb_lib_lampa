package lampa.test.tmdblib.Koin

import android.content.Context
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.model.viewmodel.repository.internet.api.JsonTmdbPlaceHolderApi
import lampa.test.tmdblib.model.viewmodel.repository.internet.interceptor.CashInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single { getListInterceptor() }
    single { getOkkHttp(get(), get()) }
    single { getRetrofit(get()) }
    single { provideJsonPlaceHolderTmdb(get()) }
}

private fun getListInterceptor(): List<Interceptor> {

    val interceptorDebug = HttpLoggingInterceptor()
    interceptorDebug.level = HttpLoggingInterceptor.Level.BODY
    val cashInterceptor = CashInterceptor()

    return listOf(
        interceptorDebug,
        cashInterceptor
    )
}

private fun getOkkHttp(listInterceptor: List<Interceptor>, context: Context): OkHttpClient {
    val client = OkHttpClient.Builder()
    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(context.cacheDir, cacheSize)
    client.cache(myCache)

    for (interceptor in listInterceptor) {
        client.addInterceptor(interceptor)
    }

    return client.build()
}

private fun getRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory
                .createWithScheduler(Schedulers.io())
        )
        .client(client)
        .build()
}

private fun provideJsonPlaceHolderTmdb(retrofit: Retrofit): JsonTmdbPlaceHolderApi {
    return retrofit.create(JsonTmdbPlaceHolderApi::class.java)
}
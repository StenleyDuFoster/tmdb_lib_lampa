package lampa.test.tmdblib.model.repository.internet.api

import lampa.test.tmdblib.model.repository.data.Movie

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JsonPlaceHolderApi {

    @GET("{search}")
    fun getTopRatedMovie(@Path("search") search: String,
                         @Query("api_key") api_key: String,
                         @Query("language") language: String,
                         @Query("page") page: Int
    ): Call<Movie>?
}
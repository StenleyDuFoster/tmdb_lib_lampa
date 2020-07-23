package lampa.test.tmdblib.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceHolderApi {

    @GET("top_rated?api_key=9bb79091064ef827e213e1b974a3b718&language=&page=")
    fun getPostsMovie(@Query("language") language: String,
                      @Query("page") page: Int
    ): Call<Movie>?
}
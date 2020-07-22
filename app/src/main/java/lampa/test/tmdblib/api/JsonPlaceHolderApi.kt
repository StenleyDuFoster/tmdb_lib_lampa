package lampa.test.tmdblib.api

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceHolderApi {
//9bb79091064ef827e213e1b974a3b718
    @GET("day?api_key=9bb79091064ef827e213e1b974a3b718")
    fun getPostsMovie(): Call<Movie>?
}
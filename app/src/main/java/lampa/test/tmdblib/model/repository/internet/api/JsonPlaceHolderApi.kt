package lampa.test.tmdblib.model.repository.internet.api

import lampa.test.tmdblib.model.repository.data.Movie
import lampa.test.tmdblib.model.repository.data.PostMovieRating
import lampa.test.tmdblib.model.repository.data.PostResponse
import lampa.test.tmdblib.model.repository.data.Session

import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceHolderApi {

    @GET("movie/{search}")
    fun getListMovie(@Path("search") search: String,
                     @Query("api_key") api_key: String,
                     @Query("language") language: String,
                     @Query("page") page: Int
    ): Call<Movie>?

    @GET("{session_id}/rated/movies?")
    fun getLikeMovie(@Path("session_id") session_id: String,
                     @Query("api_key") api_key: String,
                     @Query("language") language: String,
                     @Query("sort_by") sort_by: String
    ): Call<Movie>?

    @GET("new?")
    fun getSession(@Query("api_key") api_key: String): Call<Session>?

    @POST("{movie_id}/rating?")
    fun postLikeMovie(@Path("movie_id") movie_id: Int,
                      @Query("api_key") api_key: String,
                      @Query("guest_session_id") guest_session_id: String,
                      @Body userData: PostMovieRating
    ): Call<PostResponse>

    @DELETE("{movie_id}/rating?")
    fun deleteLikeMovie(@Path("movie_id") movie_id: Int,
                        @Query("api_key") api_key: String,
                        @Query("guest_session_id") guest_session_id: String
    ): Call<PostResponse>
}
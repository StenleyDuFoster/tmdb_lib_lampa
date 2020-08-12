package lampa.test.tmdblib.repository.internet.api

import lampa.test.tmdblib.repository.data.MovieTmdbData
import lampa.test.tmdblib.repository.data.PostMovieRatingData
import lampa.test.tmdblib.repository.data.PostResponseData
import lampa.test.tmdblib.repository.data.SessionTmdbData

import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceHolderApi {

    @GET("movie/{search}")
    fun getListMovie(@Path("search") search: String,
                     @Query("api_key") api_key: String,
                     @Query("language") language: String,
                     @Query("page") page: Int
    ): Call<MovieTmdbData>?

    @GET("{session_id}/rated/movies?")
    fun getLikeMovie(@Path("session_id") session_id: String,
                     @Query("api_key") api_key: String,
                     @Query("language") language: String,
                     @Query("page") page: Int,
                     @Query("sort_by") sort_by: String
    ): Call<MovieTmdbData>?

    @GET("new?")
    fun getSession(@Query("api_key") api_key: String): Call<SessionTmdbData>?

    @POST("{movie_id}/rating?")
    fun postLikeMovie(@Path("movie_id") movie_id: Int,
                      @Query("api_key") api_key: String,
                      @Query("guest_session_id") guest_session_id: String,
                      @Body userData: PostMovieRatingData
    ): Call<PostResponseData>

    @DELETE("{movie_id}/rating?")
    fun deleteLikeMovie(@Path("movie_id") movie_id: Int,
                        @Query("api_key") api_key: String,
                        @Query("guest_session_id") guest_session_id: String
    ): Call<PostResponseData>
}
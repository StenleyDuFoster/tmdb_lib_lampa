package lampa.test.tmdblib.repository.internet.api

import io.reactivex.Single
import lampa.test.tmdblib.repository.data.MovieTmdbData
import lampa.test.tmdblib.repository.data.PostMovieRatingData
import lampa.test.tmdblib.repository.data.PostResponseData
import lampa.test.tmdblib.repository.data.SessionTmdbData

import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface JsonPlaceHolderApi {

    @GET("movie/{search}")
    fun getListMovie(@Path("search") search: String,
                     @Query("api_key") api_key: String,
                     @Query("language") language: String,
                     @Query("page") page: Int
    ): Single<MovieTmdbData>

    @GET("guest_session/{session_id}/rated/movies?")
    fun getLikeMovie(@Path("session_id") session_id: String,
                     @Query("api_key") api_key: String,
                     @Query("language") language: String,
                     @Query("page") page: Int,
                     @Query("sort_by") sort_by: String
    ): Single<MovieTmdbData>

    @GET("authentication/guest_session/new?")
    fun getSession(@Query("api_key") api_key: String): Single<SessionTmdbData>

    @POST("movie/{movie_id}/rating?")
    fun postLikeMovie(@Path("movie_id") movie_id: Int,
                      @Query("api_key") api_key: String,
                      @Query("guest_session_id") guest_session_id: String,
                      @Body userData: PostMovieRatingData
    ): Single<PostResponseData>

    @DELETE("{movie_id}/rating?")
    fun deleteLikeMovie(@Path("movie_id") movie_id: Int,
                        @Query("api_key") api_key: String,
                        @Query("guest_session_id") guest_session_id: String
    ): Single<PostResponseData>
}
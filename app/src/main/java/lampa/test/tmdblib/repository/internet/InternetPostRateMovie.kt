package lampa.test.tmdblib.repository.internet

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.contract_interface.CallBackFromInternetPostMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.repository.data.PostMovieRatingData
import lampa.test.tmdblib.repository.internet.api.JsonTmdbPlaceHolderApi
import lampa.test.tmdblib.util.constant.ApiConstant
import org.koin.core.KoinComponent
import org.koin.core.inject

class InternetPostRateMovie(val callBackFromInternetPostMovieToMovieViewModel: CallBackFromInternetPostMovieToMovieViewModel)
    : MainContract.InternetPostLikeMovie, KoinComponent {

    val jsonTmdbPlaceHolderApi:JsonTmdbPlaceHolderApi by inject()

    override fun postAddToLike(session_id:String, movie_id: Int) {

        jsonTmdbPlaceHolderApi.postLikeMovie(
                movie_id,
                ApiConstant().API_V3,
                session_id,
                PostMovieRatingData(8f))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { responseData ->
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(responseData.status_message)
                },
                { t ->
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(t.toString())
                })
    }

    override fun postDeleteLike(session_id:String, movie_id: Int) {

        jsonTmdbPlaceHolderApi.deleteLikeMovie(
                movie_id,
                ApiConstant().API_V3,
                session_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { responseData ->
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(responseData.status_message)
                },
                { t ->
                    callBackFromInternetPostMovieToMovieViewModel.onPostSuccess(t.toString())
                })
    }
}
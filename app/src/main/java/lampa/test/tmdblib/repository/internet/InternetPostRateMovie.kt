package lampa.test.tmdblib.repository.internet

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.contract_interface.CallBackFromInternetPostMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.dagger.component.DaggerComponent
import lampa.test.tmdblib.repository.data.PostMovieRatingData
import lampa.test.tmdblib.repository.internet.api.JsonPlaceHolderApi

class InternetPostRateMovie(val callBackFromInternetPostMovieToMovieViewModel: CallBackFromInternetPostMovieToMovieViewModel)
    : MainContract.InternetPostLikeMovie {

    val jsonPlaceHolderApi:JsonPlaceHolderApi

    init {
        jsonPlaceHolderApi = DaggerComponent.create().getTmdbPlaceHolderApi()
    }

    override fun postAddToLike(session_id:String, movie_id: Int) {

        jsonPlaceHolderApi.postLikeMovie(movie_id,"9bb79091064ef827e213e1b974a3b718",
            session_id, PostMovieRatingData(8f))
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

        jsonPlaceHolderApi.deleteLikeMovie(movie_id,"9bb79091064ef827e213e1b974a3b718",
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
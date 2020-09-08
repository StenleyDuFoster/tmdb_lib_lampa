package lampa.test.tmdblib.model.viewmodel.repository.internet

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.contract_interface.CallBackFromInternetPostMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.viewmodel.repository.data.PostMovieRatingData
import lampa.test.tmdblib.model.viewmodel.repository.internet.api.JsonTmdbPlaceHolderApi
import lampa.test.tmdblib.util.constant.ApiConstant
import org.koin.core.KoinComponent
import org.koin.core.inject

class InternetPostRateMovie(val callBackFromInternetPostMovieToMovieViewModel: CallBackFromInternetPostMovieToMovieViewModel)
    : MainContract.InternetPostLikeMovie, KoinComponent {

    private val compositeDisposable = CompositeDisposable()
    val jsonTmdbPlaceHolderApi:JsonTmdbPlaceHolderApi by inject()

    override fun postAddToLike(session_id:String, movie_id: Int) {

        compositeDisposable.add(
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
        )
    }

    override fun postDeleteLike(session_id:String, movie_id: Int) {

        compositeDisposable.add(
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
        )
    }

    override fun dispose() {
        if(!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
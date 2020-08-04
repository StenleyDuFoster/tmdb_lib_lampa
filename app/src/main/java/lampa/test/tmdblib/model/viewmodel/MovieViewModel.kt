package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import lampa.test.tmdblib.R
import lampa.test.tmdblib.contract_interface.CallBackFromInternetMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.CallBackFromInternetPostMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.data.Movie
import lampa.test.tmdblib.model.repository.data.WrapperMovie
import lampa.test.tmdblib.model.repository.internet.InternetMovieLoader
import lampa.test.tmdblib.model.repository.internet.InternetPostRateMovie

class MovieViewModel(application: Application) : AndroidViewModel(application), MainContract.ViewModel,
    CallBackFromInternetMovieToMovieViewModel, CallBackFromInternetPostMovieToMovieViewModel {

    var internetInternetLoadMovie: MainContract.InternetLoadMovie
    var internetPostLikeMovie: MainContract.InternetPostLikeMovie

    var showAllOrAddToShow = R.integer.ALL_PAGE

    val liveMovie: MutableLiveData<WrapperMovie> = MutableLiveData()
    val liveProgress: MutableLiveData<String> = MutableLiveData()

    val livePostStatus: MutableLiveData<String> = MutableLiveData()

    var context = application.applicationContext

    init {
        internetInternetLoadMovie =
            InternetMovieLoader(this)
        internetInternetLoadMovie?.loadPageMovie()

        internetPostLikeMovie =
            InternetPostRateMovie(this)
    }

    fun getMovie() = liveMovie
    fun getProgress() = liveProgress
    fun getPostStatus() = livePostStatus

    override fun getPage() {

        showAllOrAddToShow = R.integer.ALL_PAGE
        internetInternetLoadMovie?.loadPageMovie()
    }

    override fun addPage() {

        showAllOrAddToShow = R.integer.ADD_TO_PAGE
        internetInternetLoadMovie?.loadAddPageMovie()
    }

    override fun changeMovieType(movieType: String) {

        internetInternetLoadMovie?.setMovieType(movieType)
    }

    override fun onMovieLoad(wrapperMovie: WrapperMovie) {

        val wm = wrapperMovie
        wm.showAllOrAddToShow = showAllOrAddToShow
        liveMovie.postValue(wm)
    }

    override fun onFailure(failure: String) {
        liveProgress.postValue(failure)
    }

    override fun postLikeMovie(movie_id: Int) {

        internetPostLikeMovie.postAddToLike("4b196263ed969f7cc9a2b4a2816461a6",
                                    movie_id)
    }

    override fun postDeleteLikeMovie(movie_id: Int) {

        internetPostLikeMovie.postDeleteLike("4b196263ed969f7cc9a2b4a2816461a6",
            movie_id)
    }

    override fun getLikeMovie() {
        showAllOrAddToShow = R.integer.ALL_PAGE
        internetInternetLoadMovie?.loadLikeListMovie("4b196263ed969f7cc9a2b4a2816461a6")
    }

    override fun onPostSuccess(session_msg: String) {
        livePostStatus.postValue(session_msg)
    }
}
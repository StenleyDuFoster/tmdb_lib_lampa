package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import lampa.test.tmdblib.R
import lampa.test.tmdblib.contract_interface.CallBackFromInternetMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.CallBackFromInternetPostMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.repository.data.WrapperMovieData
import lampa.test.tmdblib.repository.internet.InternetMovieLoader
import lampa.test.tmdblib.repository.internet.InternetPostRateMovie

class MovieViewModel(application: Application) : AndroidViewModel(application), MainContract.ViewModel,
    CallBackFromInternetMovieToMovieViewModel, CallBackFromInternetPostMovieToMovieViewModel {

    var internetInternetLoadMovie: MainContract.InternetLoadMovie
    var internetPostLikeMovie: MainContract.InternetPostLikeMovie

    var showAllOrAddToShow = R.integer.ALL_PAGE

    val liveMovieData: MutableLiveData<WrapperMovieData> = MutableLiveData()
    val liveProgress: MutableLiveData<String> = MutableLiveData()

    val livePostStatus: MutableLiveData<String> = MutableLiveData()

    var context = application.applicationContext

    lateinit var session_id:String

    init {
        internetInternetLoadMovie =
            InternetMovieLoader(this)
        internetInternetLoadMovie?.loadPageMovie()

        internetPostLikeMovie =
            InternetPostRateMovie(this)
    }

    fun getMovie() = liveMovieData
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

    override fun onMovieLoad(wrapperMovieData: WrapperMovieData) {

        val wm = wrapperMovieData
        wm.showAllOrAddToShow = showAllOrAddToShow
        liveMovieData.postValue(wm)
    }

    override fun onFailure(failure: String) {
        liveProgress.postValue(failure)
    }

    override fun setSessionId(session_id: String){
        this.session_id = session_id
    }

    override fun postLikeMovie(movie_id: Int) {

        internetPostLikeMovie.postAddToLike(session_id, movie_id)
    }

    override fun postDeleteLikeMovie(movie_id: Int) {

        internetPostLikeMovie.postDeleteLike(session_id,
            movie_id)
    }

    override fun getLikeMovie() {
        showAllOrAddToShow = R.integer.ALL_PAGE
        internetInternetLoadMovie?.loadLikeListMovie(session_id)
    }

    override fun onPostSuccess(session_msg: String) {
        livePostStatus.postValue(session_msg)
    }
}
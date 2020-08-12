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
import lampa.test.tmdblib.repository.local.database.LoggedDatabase

class MovieViewModel(application: Application) : AndroidViewModel(application),
    MainContract.MovieViewModel,
    CallBackFromInternetMovieToMovieViewModel, CallBackFromInternetPostMovieToMovieViewModel {

    var internetLoadMovie: MainContract.InternetLoadMovie
    var internetPostLikeMovie: MainContract.InternetPostLikeMovie

    var showAllOrAddToShow = R.integer.ALL_PAGE

    val liveMovieData: MutableLiveData<WrapperMovieData> = MutableLiveData()
    val liveProgress: MutableLiveData<String> = MutableLiveData()

    val livePostStatus: MutableLiveData<String> = MutableLiveData()

    var context = application.applicationContext

    lateinit var session_id:String

    init {
        internetLoadMovie =
            InternetMovieLoader(this)

        internetPostLikeMovie =
            InternetPostRateMovie(this)
    }

    fun getMovie() = liveMovieData
    fun getProgress() = liveProgress
    fun getPostStatus() = livePostStatus

    override fun getPage() {

        showAllOrAddToShow = R.integer.ALL_PAGE
        internetLoadMovie?.loadPageMovie()
    }

    override fun addPage() {

        showAllOrAddToShow = R.integer.ADD_TO_PAGE
        internetLoadMovie?.loadAddPageMovie()
    }

    override fun changeMovieType(movieType: String) {

        internetLoadMovie?.setMovieType(movieType)
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
        internetLoadMovie?.loadLikeListMovie(session_id)
    }

    override fun logOut() {

        val db = LoggedDatabase.getInstance(context)
        Thread(
            Runnable {
                db.loggedInUserDao().delete()
            }
        ).start()
    }

    override fun onPostSuccess(session_msg: String) {
        livePostStatus.postValue(session_msg)
    }
}
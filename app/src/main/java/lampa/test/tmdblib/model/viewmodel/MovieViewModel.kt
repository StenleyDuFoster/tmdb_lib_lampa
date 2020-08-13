package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import android.content.Context
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
    MainContract.MovieViewModel, CallBackFromInternetMovieToMovieViewModel, CallBackFromInternetPostMovieToMovieViewModel {

    private var internetLoadMovie: MainContract.InternetLoadMovie = InternetMovieLoader(this)
    private var internetPostLikeMovie: MainContract.InternetPostLikeMovie = InternetPostRateMovie(this)

    private var showAllOrAddToShow = R.integer.ALL_PAGE

    private val liveMovieData: MutableLiveData<WrapperMovieData> = MutableLiveData()
    private val liveProgress: MutableLiveData<String> = MutableLiveData()

    private val livePostStatus: MutableLiveData<String> = MutableLiveData()

    var context: Context = application.applicationContext

    lateinit var session_id:String

    fun getMovie() = liveMovieData
    fun getProgress() = liveProgress
    fun getPostStatus() = livePostStatus

    override fun getPage() {

        showAllOrAddToShow = R.integer.ALL_PAGE
        internetLoadMovie.loadPageMovie()
    }

    override fun addPage() {

        showAllOrAddToShow = R.integer.ADD_TO_PAGE
        internetLoadMovie.loadAddPageMovie()
    }

    override fun changeMovieType(movieType: String) {

        internetLoadMovie.setMovieType(movieType)
    }

    override fun onMovieLoad(wrapperMovieData: WrapperMovieData) {

        wrapperMovieData.showAllOrAddToShow = showAllOrAddToShow
        liveMovieData.postValue(wrapperMovieData)
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
        internetLoadMovie.loadLikeListMovie(session_id)
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
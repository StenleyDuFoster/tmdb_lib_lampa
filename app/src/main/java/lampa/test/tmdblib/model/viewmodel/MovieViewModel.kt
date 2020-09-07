package lampa.test.tmdblib.model.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.schedulers.Schedulers

import lampa.test.tmdblib.R
import lampa.test.tmdblib.contract_interface.CallBackFromInternetMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.CallBackFromInternetPostMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.viewmodel.repository.data.WrapperMovieData
import lampa.test.tmdblib.model.viewmodel.repository.internet.InternetMovieLoader
import lampa.test.tmdblib.model.viewmodel.repository.internet.InternetPostRateMovie
import lampa.test.tmdblib.model.viewmodel.repository.local.database.LoggedDatabase
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieViewModel  : ViewModel(),
    MainContract.MovieViewModel, CallBackFromInternetMovieToMovieViewModel,
    CallBackFromInternetPostMovieToMovieViewModel, KoinComponent{

    private var internetLoadMovie: MainContract.InternetLoadMovie = InternetMovieLoader(this)
    private var internetPostLikeMovie: MainContract.InternetPostLikeMovie = InternetPostRateMovie(this)

    private var showAllOrAddToShow = R.integer.ALL_PAGE

    private val liveMovieData: MutableLiveData<WrapperMovieData> = MutableLiveData()
    private val liveProgress: MutableLiveData<String> = MutableLiveData()

    private val livePostStatus: MutableLiveData<String> = MutableLiveData()

    private val context: Context by inject()

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
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        db.loggedInUserDao().delete()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onPostSuccess(session_id: String) {
        livePostStatus.postValue(session_id)
    }
}
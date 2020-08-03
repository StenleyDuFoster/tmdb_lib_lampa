package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import lampa.test.tmdblib.R
import lampa.test.tmdblib.contract_interface.CallBackFromInternetMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.data.Movie
import lampa.test.tmdblib.model.repository.data.WrapperMovie
import lampa.test.tmdblib.model.repository.internet.InternetMovieLoader

class MovieViewModel(application: Application) : AndroidViewModel(application), MainContract.ViewModel, CallBackFromInternetMovieToMovieViewModel {

    var internetInternetLoadMovie: MainContract.InternetLoadMovie

    var showAllOrAddToShow = R.integer.ALL_PAGE

    val liveMovie: MutableLiveData<WrapperMovie> = MutableLiveData()
    val liveProgress: MutableLiveData<String> = MutableLiveData()

    var context = application.applicationContext

    init {
        internetInternetLoadMovie =
            InternetMovieLoader(this)
        internetInternetLoadMovie?.loadPageMovie()
    }

    fun getMovie() = liveMovie

    fun getProgress() = liveProgress

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

    override fun onMovieLoad(movie: Movie) {

        var wrapperMovie = WrapperMovie(showAllOrAddToShow, movie)
        liveMovie.postValue(wrapperMovie)
    }

    override fun onFailure(failure: String) {
        liveProgress.postValue(failure)
    }
}
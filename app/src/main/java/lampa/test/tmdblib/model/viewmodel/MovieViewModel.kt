package lampa.test.tmdblib.model.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room

import lampa.test.tmdblib.R
import lampa.test.tmdblib.contract_interface.CallBackFromRepositoryToViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.repository.data.Movie
import lampa.test.tmdblib.model.repository.data.WrapperMovie
import lampa.test.tmdblib.model.repository.internet.InternetRepository
import lampa.test.tmdblib.model.repository.local.database.AppDatabase

class MovieViewModel(application: Application) : AndroidViewModel(application), MainContract.Presenter, CallBackFromRepositoryToViewModel {

    var InternetRepository: MainContract.Repository
    var db: AppDatabase

    var showAllOrAddToShow = R.integer.ALL_PAGE

    val liveMovie: MutableLiveData<WrapperMovie> = MutableLiveData()
    val liveProgress: MutableLiveData<String> = MutableLiveData()

    var context = application.applicationContext

    init {
        InternetRepository =
            InternetRepository(this)

        db = Room.databaseBuilder(context, AppDatabase::class.java, "database")
            .build()
    }

    fun getMovie() = liveMovie

    fun getProgress() = liveProgress

    override fun getPage() {
        showAllOrAddToShow = R.integer.ALL_PAGE
        InternetRepository?.loadPageMovie()
    }

    override fun addPage() {
        showAllOrAddToShow = R.integer.ADD_TO_PAGE
        InternetRepository?.loadAddPageMovie()
    }

    override fun changeMovieType(movieType: String) {
       InternetRepository?.setMovieType(movieType)
    }

    override fun onMovieLoad(movie: Movie) {

        var wrapperMovie = WrapperMovie(showAllOrAddToShow, movie)
        liveMovie.postValue(wrapperMovie)
    }

    override fun onFailure(failure: String) {
        liveProgress.postValue(failure)
    }
}
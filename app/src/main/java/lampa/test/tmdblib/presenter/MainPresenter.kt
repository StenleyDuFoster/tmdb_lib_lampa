package lampa.test.tmdblib.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lampa.test.tmdblib.R
import lampa.test.tmdblib.contract_interface.CallBackFromRepositoryToViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.data.Movie
import lampa.test.tmdblib.model.data.WrapperMovie
import lampa.test.tmdblib.model.repository.MainRepository


class MainPresenter: ViewModel(), MainContract.Presenter, CallBackFromRepositoryToViewModel {

    //var mView: MainContract.View? = null
    var Model: MainContract.Repository? = null
    var showAllOrAddToShow = R.integer.ALL_PAGE
    val liveMovie: MutableLiveData<WrapperMovie> = MutableLiveData()

    init {
        //this.mView = mView
        Model = MainRepository(this)
    }

    fun getMovie() = liveMovie

    override fun getPage() {
        showAllOrAddToShow = R.integer.ALL_PAGE
        Model?.loadMovie()
    }

    override fun addPage() {
        showAllOrAddToShow = R.integer.ADD_TO_PAGE
        Model?.loadMovie()
    }

    override fun changeMovieType(movieType: String) {
       Model?.setMovieType(movieType)
    }

    override fun onMovieLoad(movie: Movie) {

        var wrapperMovie = WrapperMovie(showAllOrAddToShow, movie)
        liveMovie?.postValue(wrapperMovie)

//        if(showAllOrAddToShow == R.integer.ALL_PAGE)
//            mView?.showPage(movie.results)
//        else if(showAllOrAddToShow == R.integer.ADD_TO_PAGE)
//            mView?.addToShow(movie.results)
    }
}
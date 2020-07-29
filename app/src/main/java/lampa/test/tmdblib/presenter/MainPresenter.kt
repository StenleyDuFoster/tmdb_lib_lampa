package lampa.test.tmdblib.presenter

import lampa.test.tmdblib.R
import lampa.test.tmdblib.contract_interface.CallBackFromRepositoryToMainContract
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.MainRepository
import lampa.test.tmdblib.model.data.Movie

class MainPresenter(mView: MainContract.View?): MainContract.Presenter, CallBackFromRepositoryToMainContract {

    var mView: MainContract.View? = null
    var Model: MainContract.Repository? = null
    var showAllOrAddToShow = R.integer.ALL_PAGE

    init {
        this.mView = mView
        Model = MainRepository(this)
    }

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

        if(showAllOrAddToShow == R.integer.ALL_PAGE)
            mView?.showPage(movie.results)
        else if(showAllOrAddToShow == R.integer.ADD_TO_PAGE)
            mView?.addToShow(movie.results)
    }
}
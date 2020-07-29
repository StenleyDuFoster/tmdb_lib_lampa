package lampa.test.tmdblib.presenter

import lampa.test.tmdblib.contract_interface.CallBackFromRepositoryToMainContract
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.MainRepository


class MainPresenter(mView: MainContract.View?): MainContract.Presenter, CallBackFromRepositoryToMainContract {

    var mView: MainContract.View? = null
    var mRepository: MainContract.Repository? = null


    init {
        this.mView = mView
        mRepository = MainRepository(this)
    }


    override fun getPage() {
        TODO("Not yet implemented")
    }

    override fun addPage() {
        TODO("Not yet implemented")
    }

    override fun changeMarkup(markup: Int) {
        TODO("Not yet implemented")
    }

    override fun changeMovieType(movieType: Int) {
        TODO("Not yet implemented")
    }

    override fun onMovieLoad() {
        TODO("Not yet implemented")
    }
}
package lampa.test.tmdblib.mvp.repository

import lampa.test.tmdblib.mvp.api.Movie
import lampa.test.tmdblib.mvp.contract.MainContract

class MainRepository: MainContract.Repository {

    override fun loadMovie(): Movie? {
        return null
    }
}
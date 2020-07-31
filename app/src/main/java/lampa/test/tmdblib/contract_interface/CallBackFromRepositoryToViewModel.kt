package lampa.test.tmdblib.contract_interface

import lampa.test.tmdblib.model.repository.data.Movie

interface CallBackFromRepositoryToViewModel {

    fun onMovieLoad(movie: Movie)

    fun onFailure(failure: String)
}
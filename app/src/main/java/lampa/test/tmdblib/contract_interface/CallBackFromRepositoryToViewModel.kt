package lampa.test.tmdblib.contract_interface

import lampa.test.tmdblib.model.data.Movie

interface CallBackFromRepositoryToViewModel {

    fun onMovieLoad(movie: Movie)
}
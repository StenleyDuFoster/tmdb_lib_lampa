package lampa.test.tmdblib.contract_interface

import lampa.test.tmdblib.model.repository.data.WrapperMovie

interface CallBackFromInternetMovieToMovieViewModel {

    fun onMovieLoad(wrapperMovie: WrapperMovie)

    fun onFailure(failure: String)
}
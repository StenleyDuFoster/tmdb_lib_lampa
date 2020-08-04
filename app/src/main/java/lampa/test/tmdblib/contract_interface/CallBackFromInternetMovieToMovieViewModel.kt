package lampa.test.tmdblib.contract_interface

import lampa.test.tmdblib.model.repository.data.WrapperMovieData

interface CallBackFromInternetMovieToMovieViewModel {

    fun onMovieLoad(wrapperMovieData: WrapperMovieData)

    fun onFailure(failure: String)
}
package lampa.test.tmdblib.contract_interface

import lampa.test.tmdblib.model.viewmodel.repository.data.WrapperMovieData

interface CallBackFromInternetMovieToMovieViewModel {

    fun onMovieLoad(wrapperMovieData: WrapperMovieData)

    fun onFailure(failure: String)
}
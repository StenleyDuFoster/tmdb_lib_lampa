package lampa.test.tmdblib.contract_interface

import lampa.test.tmdblib.model.data.Results

interface MainContract {

    interface View {

        fun showPage(res: List<Results>)
        fun addToShow(res: List<Results>)

    }

    interface Presenter {

        fun getPage()
        fun addPage()
        fun changeMovieType(movieType: String)
    }

    interface Repository {

        fun loadMovie()
        fun setMovieType(movieType: String)
    }
}
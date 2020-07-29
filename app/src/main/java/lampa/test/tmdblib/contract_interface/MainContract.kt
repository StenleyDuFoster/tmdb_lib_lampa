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
        fun changeMarkup(markup: Int)
        fun changeMovieType(movieType: Int)
    }

    interface Repository {

        fun loadMovie()
    }
}
package lampa.test.tmdblib.contract_interface

interface MainContract {

    interface Presenter {

        fun getPage()
        fun addPage()
        fun changeMovieType(movieType: String)
    }

    interface Repository {

        fun loadPageMovie()
        fun loadAddPageMovie()
        fun setMovieType(movieType: String)
    }
}
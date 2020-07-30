package lampa.test.tmdblib.contract_interface

interface MainContract {

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
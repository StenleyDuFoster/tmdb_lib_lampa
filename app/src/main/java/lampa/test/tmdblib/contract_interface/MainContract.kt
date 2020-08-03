package lampa.test.tmdblib.contract_interface

interface MainContract {

    interface ViewModel {

        fun getPage()
        fun addPage()
        fun changeMovieType(movieType: String)
    }

    interface InternetLoadMovie {

        fun loadPageMovie()
        fun loadAddPageMovie()
        fun setMovieType(movieType: String)
    }

    interface InternetAuth {

        fun createSession()
    }
}
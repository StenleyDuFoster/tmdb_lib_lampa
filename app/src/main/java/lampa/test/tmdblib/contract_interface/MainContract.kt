package lampa.test.tmdblib.contract_interface

interface MainContract {

    interface ViewModel {

        fun getPage()
        fun addPage()
        fun changeMovieType(movieType: String)
        fun postLikeMovie(movie_id: Int)
    }

    interface InternetLoadMovie {

        fun loadPageMovie()
        fun loadAddPageMovie()
        fun setMovieType(movieType: String)
    }

    interface InternetAuth {

        fun createSession()
    }

    interface InternetPostLikeMovie {

        fun post(session_id:String, movie_id: Int)
    }
}
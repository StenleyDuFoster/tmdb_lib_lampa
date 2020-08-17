package lampa.test.tmdblib.contract_interface

interface MainContract {

    interface MovieViewModel {

        fun getPage()
        fun addPage()
        fun setSessionId(session_id: String)
        fun changeMovieType(movieType: String)
        fun postLikeMovie(movie_id: Int)
        fun postDeleteLikeMovie(movie_id: Int)
        fun getLikeMovie()

        fun logOut()
    }

    interface InternetLoadMovie {

        fun loadPageMovie()
        fun loadAddPageMovie()
        fun loadLikeListMovie(session_id: String)
        fun setMovieType(movieType: String)
    }

    interface InternetAuth {

        fun startAuth()
    }

    interface InternetPostLikeMovie {

        fun postAddToLike(session_id:String, movie_id: Int)
        fun postDeleteLike(session_id:String, movie_id: Int)
    }
}
package lampa.test.tmdblib.repository.data

    data class WrapperMovieData (

        var showAllOrAddToShow:Int,
        val movieTmdbData: MovieTmdbData,
        val toLikeList: Boolean
    )
package lampa.test.tmdblib.model.repository.data

    data class WrapperMovieData (

        var showAllOrAddToShow:Int,
        val movieTmdbData: MovieTmdbData,
        val toLikeList: Boolean
    )
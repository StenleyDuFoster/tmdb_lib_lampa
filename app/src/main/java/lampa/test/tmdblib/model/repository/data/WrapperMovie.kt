package lampa.test.tmdblib.model.repository.data

    data class WrapperMovie (

        var showAllOrAddToShow:Int,
        val movie: Movie,
        val toLikeList: Boolean
    )
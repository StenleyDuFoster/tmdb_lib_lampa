package lampa.test.tmdblib.model.repository.data

    data class MovieTmdbData (

        val page: Int,
        val total_pages: Int,
        val results: List<MovieResultsTmdbData>
    )
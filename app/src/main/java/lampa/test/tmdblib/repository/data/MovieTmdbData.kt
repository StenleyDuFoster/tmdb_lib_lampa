package lampa.test.tmdblib.repository.data

    data class MovieTmdbData (

        val page: Int,
        val total_pages: Int,
        val results: List<MovieResultsTmdbData>
    )
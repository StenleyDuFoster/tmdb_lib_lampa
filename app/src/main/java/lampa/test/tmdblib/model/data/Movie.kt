package lampa.test.tmdblib.model.data

    data class Movie (
        val page: Int,
        val total_pages: Int,
        val results: List<Results>
    )
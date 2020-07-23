package lampa.test.tmdblib.api

    data class Movie (
        val page: Int,
        val results: List<Results>
    )

    data class Results (
        val original_name: String,
        val name: String,
        val id: Int,
        val overview: String,
        val genre_ids: Array<Int>,
        val vote_average: Float,
        val backdrop_path: String,
        val poster_path: String,
        val first_air_date: String
    )
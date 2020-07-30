package lampa.test.tmdblib.model.data

    data class Results (

        val original_name: String,
        val name: String,
        val id: Int,
        val title: String,
        val overview: String,
        val genre_ids: Array<Int>,
        val vote_average: Float,
        val backdrop_path: String,
        val poster_path: String,
        val release_date: String
    )
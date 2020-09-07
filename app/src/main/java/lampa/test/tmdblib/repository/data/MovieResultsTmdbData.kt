package lampa.test.tmdblib.repository.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResultsTmdbData (

        val original_name: String = "",
        val name: String = "",
        val id: Int = 0,
        val title: String = "",
        val overview: String = "",
        val genre_ids: Array<Int> = arrayOf(),
        val vote_average: Float = 0.0f,
        val backdrop_path: String = "",
        val poster_path: String = "",
        val release_date: String = ""
    ) : Parcelable
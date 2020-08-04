package lampa.test.tmdblib.fragments.callback

import lampa.test.tmdblib.model.repository.data.MovieResultsTmdbData

interface CallBackFromMainFToActivity {

    fun openMovie(movie: MovieResultsTmdbData)
}
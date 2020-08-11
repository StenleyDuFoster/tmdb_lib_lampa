package lampa.test.tmdblib.view.fragments.callback

import lampa.test.tmdblib.repository.data.MovieResultsTmdbData

interface CallBackFromMainFToActivity {

    fun openMovie(movie: MovieResultsTmdbData)
}
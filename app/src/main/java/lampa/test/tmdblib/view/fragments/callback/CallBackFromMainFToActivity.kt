package lampa.test.tmdblib.view.fragments.callback

import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData

interface CallBackFromMainFToActivity {

    fun openMovie(movie: MovieResultsTmdbData)
}
package lampa.test.tmdblib.fragments.callback

import lampa.test.tmdblib.api.Results

interface CallBackFromFragmentToActivity {

    fun openMovie(movie: Results)
    fun addMovieToList()
}
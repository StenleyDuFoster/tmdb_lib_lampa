package lampa.test.tmdblib.fragments.callback

import lampa.test.tmdblib.model.data.Results

interface CallBackFromFragmentToActivity {

    fun openMovie(movie: Results)
}
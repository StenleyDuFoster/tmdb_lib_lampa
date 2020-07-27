package lampa.test.tmdblib.recycler.callback

interface CallBackFromRecyclerToFragment {

    fun onMovieClick(position: Int)
    fun onFavoriteClick(position: Int)
}
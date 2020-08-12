package lampa.test.tmdblib.view.recycler.callback

interface CallBackFromRecyclerToFragment {

    fun onMovieClick(position: Int)
    fun onFavoriteClick(position: Int)
}
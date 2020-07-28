package lampa.test.tmdblib.mvp.contract

import lampa.test.tmdblib.mvp.api.Movie

interface MainContract {

    interface View {
        fun showText()
    }

    interface Presenter {
        fun onButtonWasClicked()
        fun onDestroy()
    }

    interface Repository {
        fun loadMovie(): Movie?
    }
}
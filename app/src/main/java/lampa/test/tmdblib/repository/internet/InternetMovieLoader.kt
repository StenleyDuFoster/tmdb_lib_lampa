package lampa.test.tmdblib.repository.internet

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lampa.test.tmdblib.contract_interface.CallBackFromInternetMovieToMovieViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.dagger.component.DaggerComponent
import lampa.test.tmdblib.repository.internet.api.JsonPlaceHolderApi
import lampa.test.tmdblib.repository.data.WrapperMovieData
import lampa.test.tmdblib.utils.constant.ApiConstant

class InternetMovieLoader(val callBackFromInternetMovieToMovieViewModel: CallBackFromInternetMovieToMovieViewModel)
    : MainContract.InternetLoadMovie {

    private var page: Int = 1
    private var totalPage: Int = 1
    private var searchTypeMovie: String = "popular"
    private var ADD_TO_FAVORITE = false
    private lateinit var session_id: String

    private val jsonPlaceHolderApi:JsonPlaceHolderApi

    init {
        jsonPlaceHolderApi = DaggerComponent.create().getTmdbPlaceHolderApiByRetrofit()
    }

    override fun setMovieType(movieType: String){
        searchTypeMovie = movieType
        loadPageMovie()
    }

    fun loadListMovie() {

        jsonPlaceHolderApi.getListMovie(
                searchTypeMovie,
                ApiConstant().API_V3,
                "ru",
                page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { movieTmdbData ->

                    callBackFromInternetMovieToMovieViewModel.onMovieLoad(
                        WrapperMovieData(
                            0,movieTmdbData,ADD_TO_FAVORITE)
                    )
                },
                { t ->
                    callBackFromInternetMovieToMovieViewModel.onFailure(t.toString())
                })
    }

    fun loadLikeMovie(){

        jsonPlaceHolderApi.getLikeMovie(
                session_id,
                ApiConstant().API_V3,
                "ru",
                page,
                ApiConstant().API_SORT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { movieTmdbData ->
                    var wrapperMovie = WrapperMovieData(0,movieTmdbData,ADD_TO_FAVORITE)
                    callBackFromInternetMovieToMovieViewModel.onMovieLoad(wrapperMovie)
                },
                {t ->
                    callBackFromInternetMovieToMovieViewModel.onFailure(t.toString())
                })
    }

    override fun loadPageMovie() {

        ADD_TO_FAVORITE = false
        page = 1
        loadListMovie()
    }

    override fun loadAddPageMovie() {

        page += 1
        if(totalPage <= page) {

            if (!ADD_TO_FAVORITE)
                loadListMovie()
            else
                loadLikeMovie()
        }
    }

    override fun loadLikeListMovie(session_id: String) {

        page = 1
        ADD_TO_FAVORITE = true
        this.session_id = session_id
        loadLikeMovie()
    }
}
package lampa.test.tmdblib.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.MovieViewModel
import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.model.viewmodel.repository.data.WrapperMovieData
import lampa.test.tmdblib.util.anim.CustomAnimate
import lampa.test.tmdblib.util.toast.makeToast
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.base.BaseFragment
import lampa.test.tmdblib.view.fragments.callback.CallBackFromMainFToActivity
import lampa.test.tmdblib.view.recycler.MovieRecyclerAdapter
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment

class FragmentMain : BaseFragment(R.layout.fragment_main), CallBackFromRecyclerToFragment {

    lateinit var movieViewModel: MovieViewModel

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private var adapterMovie = MovieRecyclerAdapter()

    private lateinit var callBackFromMainFToActivity: CallBackFromMainFToActivity

    private var allContent: ArrayList<MovieResultsTmdbData> = ArrayList()

    private var isDownload = false
    private var isLikeListOpen = false

    var session = "session"
    private lateinit var searchTypeMovie: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        callBackFromMainFToActivity = activity as CallBackFromMainFToActivity

        initRecycler(view)
        initViewModelObservers()
        initConnectionManager()
        initButtonLayoutManager()
        initButtonMyLikeList()
        initSpinner()
        movieViewModel.setSessionId(session)
    }

    private fun initViewModelObservers() {

        movieViewModel.getMovie()
            .observe(viewLifecycleOwner, Observer { wrapperMovieData: WrapperMovieData ->

                val resultArray = ArrayList(wrapperMovieData.movieTmdbData.results)

                if (wrapperMovieData.showAllOrAddToShow == R.integer.ALL_PAGE) {
                    allContent = resultArray
                    (recycler.adapter as MovieRecyclerAdapter).mExampleList.clear()
                    (recycler.adapter as MovieRecyclerAdapter).mExampleList.addAll(allContent)
                    (recycler.adapter as MovieRecyclerAdapter).notifyDataSetChanged()
                    isDownload = false
                    CustomAnimate.recycler(recycler)
                    CustomAnimate.scale(activity?.progress_bar!!, 0.0f)
                } else {
                    allContent.addAll(resultArray)
                    (recycler.adapter as MovieRecyclerAdapter).mExampleList.addAll(resultArray)
                    (recycler.adapter as MovieRecyclerAdapter).notifyDataSetChanged()
                    isDownload = false
                    CustomAnimate.scale(activity?.progress_bar!!, 0.0f)
                }

                isLikeListOpen = wrapperMovieData.toLikeList
            })

        movieViewModel.getProgress()
            .observe(viewLifecycleOwner, Observer { failure: String ->
                makeToast(failure)
                CustomAnimate.scale(activity?.progress_bar!!, 0.0f)
            })

        movieViewModel.getPostStatus().observe(viewLifecycleOwner, Observer { msg: String ->
            makeToast(msg)
        })
    }


    private fun initRecycler(v: View) {

        linearLayoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        var orientation = 3

        if (resources.configuration.orientation == 2) {
            orientation = 5
        }

        adapterMovie.mListener = this
        adapterMovie.itemType = MovieRecyclerAdapter.LINEAR

        gridLayoutManager =
            GridLayoutManager(v.context, orientation, GridLayoutManager.VERTICAL, false)
        gridLayoutManager.spanSizeLookup

        recycler.layoutManager = linearLayoutManager
        recycler.adapter = adapterMovie

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (((recycler.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition() > recycler.adapter?.itemCount!! / 2)
                    && !isDownload
                ) {

                    isDownload = true
                    movieViewModel.addPage()
                }
            }
        })
    }

    private fun initConnectionManager() {

        val getPage = Runnable {
            if (allContent.size < 10)
                getPage()
        }

        (activity!! as BaseActivity).networkChangeReceiver.setRunnableCode(getPage)
    }

    private fun getPage() {

        movieViewModel.getPage()
        CustomAnimate.scale(activity?.progress_bar!!, 1.0f)
    }

    fun changeMovieTypeFromFragment(movieType: String) {

        movieViewModel.changeMovieType(movieType)
        getPage()
    }

    private fun getMyLikeList() {

        movieViewModel.getLikeMovie()
    }

    private fun initButtonLayoutManager() {

        val clickListenerLayoutManager = View.OnClickListener { v ->
            when (v.id) {
                R.id.buttonLinear -> {
                    CustomAnimate.scale(buttonLinear, 1.0f)
                    CustomAnimate.scale(buttonGrid, 0.6f)
                    val position =
                        (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    (recycler.adapter as MovieRecyclerAdapter).itemType =
                        MovieRecyclerAdapter.LINEAR
                    recycler.layoutManager = linearLayoutManager
                    (recycler.adapter as MovieRecyclerAdapter).notifyDataSetChanged()
                    (recycler.layoutManager as LinearLayoutManager).scrollToPosition(position)
                }
                R.id.buttonGrid -> {
                    CustomAnimate.scale(buttonGrid, 1.0f)
                    CustomAnimate.scale(buttonLinear, 0.6f)
                    val position =
                        (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    (recycler.adapter as MovieRecyclerAdapter).itemType = MovieRecyclerAdapter.GRID
                    recycler.layoutManager = gridLayoutManager
                    (recycler.adapter as MovieRecyclerAdapter).notifyDataSetChanged()
                    (recycler.layoutManager as LinearLayoutManager).scrollToPosition(position)
                }
            }
        }
        buttonLinear.setOnClickListener(clickListenerLayoutManager)
        buttonGrid.setOnClickListener(clickListenerLayoutManager)
    }

    private fun initButtonMyLikeList() {

        buttonMyLikeList.setOnClickListener {

            getMyLikeList()
        }
    }

    private fun initSpinner() {

        val spinnerItems: Array<String> = arrayOf(
            getString(R.string.search_now_playing),
            getString(R.string.search_popular),
            getString(R.string.search_top_rated),
            getString(R.string.search_upcoming)
        )
        val spinnerUserVisible: Array<String> = arrayOf(
            "сейчас в прокате",
            "популярные",
            "лучшие оценки",
            "скоро выйдут"
        )
        val arraySpinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerUserVisible
            )
        arraySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arraySpinnerAdapter

        val spinnerItemSelectedListener: AdapterView.OnItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchTypeMovie = spinnerItems[position]
                changeMovieTypeFromFragment(spinnerItems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner.onItemSelectedListener = spinnerItemSelectedListener
    }

    override fun onMovieClick(position: Int) {
        callBackFromMainFToActivity.openMovie(allContent[position])
    }

    override fun onFavoriteClick(position: Int) {

        if (!isLikeListOpen)
            movieViewModel.postLikeMovie(allContent[position].id)
        else {
            movieViewModel.postDeleteLikeMovie(allContent[position].id)
            allContent.removeAt(position)
            recycler.adapter?.notifyDataSetChanged()
        }
    }
}
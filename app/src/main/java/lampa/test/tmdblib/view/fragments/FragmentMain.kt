package lampa.test.tmdblib.view.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import lampa.test.tmdblib.R
import lampa.test.tmdblib.view.fragments.callback.CallBackFromMainFToActivity
import lampa.test.tmdblib.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.repository.data.WrapperMovieData
import lampa.test.tmdblib.model.viewmodel.MovieViewModel
import lampa.test.tmdblib.utils.anim.CustomAnimate
import lampa.test.tmdblib.utils.connection_manager.ConnectionManager
import lampa.test.tmdblib.view.fragments.base.BaseFragment
import lampa.test.tmdblib.view.recycler.MovieRecyclerAdapter
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment

class FragmentMain(session: String) : BaseFragment(R.layout.fragment_main), CallBackFromRecyclerToFragment {

    lateinit var movieViewModel: MovieViewModel

    lateinit var recycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapterMovie: MovieRecyclerAdapter

    private val animateClass = CustomAnimate()

    private lateinit var callBackFromMainFToActivity: CallBackFromMainFToActivity

    private var allContent: ArrayList<MovieResultsTmdbData> = ArrayList()

    private lateinit var progressBar: ProgressBar

    private var isDownload = false
    private var isLikeListOpen = false

    private var session = session

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        movieViewModel = ViewModelProvider(activity!!).get(MovieViewModel::class.java)

        progressBar = activity?.findViewById(R.id.progress_bar)!!

        initRecycler(view)
        initViewModelObservers()
        initConnectionManager()
        movieViewModel.setSessionId(session)
    }

    private fun initViewModelObservers(){

        movieViewModel.getMovie().observe(viewLifecycleOwner, Observer { wrapperMovieData: WrapperMovieData ->

            val resultArray = ArrayList(wrapperMovieData.movieTmdbData.results)

            if(wrapperMovieData.showAllOrAddToShow == R.integer.ALL_PAGE) {

                allContent = resultArray
                adapterMovie = MovieRecyclerAdapter(allContent,
                                                   defineType(recycler.layoutManager),
                                                   this as CallBackFromRecyclerToFragment)
                recycler.adapter = adapterMovie
                animateClass.recycler(recycler)
                animateClass.scale(progressBar, 0.0f)
            }
            else {

                allContent.addAll(resultArray)
                recycler.adapter?.notifyItemRangeInserted(recycler.adapter!!.itemCount,
                    recycler.adapter!!.itemCount + 20)
                isDownload = false
                animateClass.scale(progressBar, 0.0f)
            }

            isLikeListOpen = wrapperMovieData.toLikeList
        })

        movieViewModel.getProgress().observe(viewLifecycleOwner, Observer { failure: String ->

            Toast.makeText(context, failure, Toast.LENGTH_LONG).show()
            animateClass.scale(progressBar, 0.0f)
        })

        movieViewModel.getPostStatus().observe(viewLifecycleOwner, Observer { msg: String ->
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
        })
    }

    private fun initRecycler(v: View){

        linearLayoutManager = LinearLayoutManager(v.context,LinearLayoutManager.VERTICAL,false)
        var orientation = 3

        if(resources.configuration.orientation == 2)
            orientation = 5

        gridLayoutManager = GridLayoutManager(v.context,orientation,GridLayoutManager.VERTICAL,false)
        gridLayoutManager.spanSizeLookup

        recycler = v.findViewById(R.id.recycler)
        recycler.layoutManager = linearLayoutManager

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (((recycler.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition() > recycler.adapter?.itemCount!!/2)
                        && !isDownload) {

                    isDownload = true
                   movieViewModel.addPage()
                }
            }
        })
    }

    private fun initConnectionManager() {

        val getPage = Runnable {
            if(allContent.size < 10)
                getPage()
        }
        val connectionManager = ConnectionManager(context!!, getPage)
        connectionManager.checkInternet()
    }

    fun getPage() {

        movieViewModel.getPage()
        allContent = ArrayList()
        allContent.clear()

        adapterMovie = MovieRecyclerAdapter(allContent, 1, this as CallBackFromRecyclerToFragment)
        recycler.adapter = adapterMovie

        animateClass.scale(progressBar, 1.0f)
    }

    fun changeMovieTypeFromFragment(movieType: String) {

        movieViewModel.changeMovieType(movieType)
        getPage()
    }

    fun setLayoutManager(type: Int){

        val oldScrollPos = (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        when (type){
            1 -> recycler.layoutManager = linearLayoutManager
            2 -> recycler.layoutManager = gridLayoutManager
        }
        adapterMovie.type = type
        recycler.adapter = adapterMovie
        animateClass.recycler(recycler)
        recycler.scrollToPosition(oldScrollPos)
    }

    private fun defineType(layoutManager: RecyclerView.LayoutManager?): Int{

        return when (layoutManager) {
            linearLayoutManager -> 1
            else -> 2
        }
    }

    fun getMyLikeList(){

        movieViewModel.getLikeMovie()
    }

    override fun onAttach(activity: Activity) {
        callBackFromMainFToActivity = activity as CallBackFromMainFToActivity
        super.onAttach(activity)
    }

    override fun onMovieClick(position: Int) {
        callBackFromMainFToActivity.openMovie(allContent[position])
    }

    override fun onFavoriteClick(position: Int) {

        if(!isLikeListOpen)
            movieViewModel.postLikeMovie(allContent[position].id)
        else {
            movieViewModel.postDeleteLikeMovie(allContent[position].id)
            allContent.removeAt(position)
            recycler.adapter?.notifyDataSetChanged()
        }
    }
}
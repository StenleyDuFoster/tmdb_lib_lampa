package lampa.test.tmdblib.view.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar

import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*

import lampa.test.tmdblib.R
import lampa.test.tmdblib.view.fragments.callback.CallBackFromMainFToActivity
import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.model.viewmodel.repository.data.WrapperMovieData
import lampa.test.tmdblib.model.viewmodel.MovieViewModel
import lampa.test.tmdblib.util.anim.CustomAnimate
import lampa.test.tmdblib.util.toast.makeToast
import lampa.test.tmdblib.view.activity.base.BaseActivity
import lampa.test.tmdblib.view.fragments.base.BaseFragment
import lampa.test.tmdblib.view.recycler.MovieRecyclerAdapter
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment

class FragmentMain : BaseFragment(R.layout.fragment_main), CallBackFromRecyclerToFragment {

    lateinit var movieViewModel: MovieViewModel

    lateinit var recycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapterMovie: MovieRecyclerAdapter

    private lateinit var callBackFromMainFToActivity: CallBackFromMainFToActivity

    private var allContent: ArrayList<MovieResultsTmdbData> = ArrayList()

    private lateinit var progressBar: ProgressBar

    private var isDownload = false
    private var isLikeListOpen = false

    var session = "session"
    private lateinit var searchTypeMovie: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        progressBar = activity?.findViewById(R.id.progress_bar)!!

        initRecycler(view)
        initViewModelObservers()
        initConnectionManager()
        initButtonLayoutManager()
        initButtonMyLikeList()
        initSpinner()
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
                CustomAnimate.recycler(recycler)
                CustomAnimate.scale(progressBar, 0.0f)
            }
            else {

                allContent.addAll(resultArray)
                recycler.adapter?.notifyItemRangeInserted(recycler.adapter!!.itemCount,
                    recycler.adapter!!.itemCount + 20)
                isDownload = false
                CustomAnimate.scale(progressBar, 0.0f)
            }

            isLikeListOpen = wrapperMovieData.toLikeList
        })

        movieViewModel.getProgress().observe(viewLifecycleOwner, Observer { failure: String ->

            makeToast(failure)
            CustomAnimate.scale(progressBar, 0.0f)
        })

        movieViewModel.getPostStatus().observe(viewLifecycleOwner, Observer { msg: String ->
            makeToast(msg)
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

        (activity!! as BaseActivity).networkChangeReceiver.setRunnableCode(getPage)
    }

    fun getPage() {

        movieViewModel.getPage()
        allContent = ArrayList()
        allContent.clear()

        adapterMovie = MovieRecyclerAdapter(allContent, 1, this as CallBackFromRecyclerToFragment)
        recycler.adapter = adapterMovie

        CustomAnimate.scale(progressBar, 1.0f)
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
        CustomAnimate.recycler(recycler)
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

    private fun initButtonLayoutManager() {

        val clickListenerLayoutManager = View.OnClickListener{v ->
            when(v.id){
                R.id.buttonLinear ->
                {
                    CustomAnimate.scale(buttonLinear,1.0f)
                    CustomAnimate.scale(buttonGrid,0.6f)
                    setLayoutManager(1)
                }
                R.id.buttonGrid ->
                {
                    CustomAnimate.scale(buttonGrid,1.0f)
                    CustomAnimate.scale(buttonLinear,0.6f)
                    setLayoutManager(2)
                }
            }
        }
        buttonLinear.setOnClickListener(clickListenerLayoutManager)
        buttonGrid.setOnClickListener(clickListenerLayoutManager)
    }

    private fun initButtonMyLikeList(){

        buttonMyLikeList.setOnClickListener {

            getMyLikeList()
        }
    }

    private fun initSpinner(){

        val spinnerItems:Array<String> = arrayOf(
            getString(R.string.search_now_playing),
            getString(R.string.search_popular),
            getString(R.string.search_top_rated),
            getString(R.string.search_upcoming)
        )
        val spinnerUserVisible:Array<String> = arrayOf(
            "сейчас в прокате",
            "популярные",
            "лучшие оценки",
            "скоро выйдут"
        )
        val arraySpinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, spinnerUserVisible)
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
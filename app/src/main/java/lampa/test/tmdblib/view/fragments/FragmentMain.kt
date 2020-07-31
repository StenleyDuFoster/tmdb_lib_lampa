package lampa.test.tmdblib.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import lampa.test.tmdblib.R
import lampa.test.tmdblib.fragments.callback.CallBackFromFragmentToActivity
import lampa.test.tmdblib.model.repository.data.Results
import lampa.test.tmdblib.model.repository.data.WrapperMovie
import lampa.test.tmdblib.model.viewmodel.MovieViewModel
import lampa.test.tmdblib.utils.anim.Animate
import lampa.test.tmdblib.utils.connection_manager.ConnectionManager
import lampa.test.tmdblib.view.recycler.RecyclerAdapter
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment

class FragmentMain : Fragment(), CallBackFromRecyclerToFragment {

    private lateinit var userViewModel: MovieViewModel

    private lateinit var recycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapter: RecyclerAdapter

    private val animateClass = Animate()

    private lateinit var callBackFromFragmentToActivity: CallBackFromFragmentToActivity

    private var allContent: ArrayList<Results> = ArrayList()

    private lateinit var progressBar: ProgressBar

    private var isDownload:Boolean = false

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_main, null)
        recycler = v.findViewById(R.id.recycler)

        linearLayoutManager = LinearLayoutManager(v.context,LinearLayoutManager.VERTICAL,false)
        gridLayoutManager = GridLayoutManager(context,3)
        progressBar = activity?.findViewById(R.id.progress_bar)!!

        initRecycler()
        initViewModel()
        initConnectionManager()

        return v
    }

    private fun initViewModel(){

        userViewModel = ViewModelProvider.NewInstanceFactory().create(MovieViewModel::class.java)

        userViewModel.getMovie().observe(viewLifecycleOwner, Observer { wrapperMovie: WrapperMovie ->

            val result_array = ArrayList(wrapperMovie.movie.results)

            if(wrapperMovie.showAllOrAddToShow == R.integer.ALL_PAGE) {

                allContent = result_array
                adapter = RecyclerAdapter(allContent,
                                                   defineType(recycler.layoutManager),
                                                   this as CallBackFromRecyclerToFragment)
                recycler.adapter = adapter
                animateClass.recycler(recycler)
                animateClass.scale(progressBar, 0.0f)
            }
            else {

                allContent.addAll(result_array)
                recycler.adapter?.notifyItemRangeInserted(recycler.adapter!!.itemCount,
                    recycler.adapter!!.itemCount + 20)
                isDownload = false
                animateClass.scale(progressBar, 0.0f)
            }
        })

        userViewModel.getProgress().observe(viewLifecycleOwner, Observer { failure: String ->

            Toast.makeText(context, failure, Toast.LENGTH_LONG).show()
            animateClass.scale(progressBar, 0.0f)
        })
    }

    private fun initRecycler(){

        recycler.layoutManager = linearLayoutManager

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (((recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() > recycler.adapter?.itemCount!!/2) &&
                    !isDownload) {
                    isDownload = true
                    val runnableCode = object: Runnable {
                        override fun run() {
                            userViewModel.addPage()
                        }
                    }
                    runnableCode.run()
                }
            }
        })
    }

    private fun initConnectionManager() {

        val get_page = Runnable { getPage() }
        val connectionManager = ConnectionManager(context!!, get_page)
        connectionManager.checkInternet()
    }

    fun getPage() {

        userViewModel.getPage()
        allContent = ArrayList()
        allContent.clear()

        adapter = RecyclerAdapter(allContent, 1, this as CallBackFromRecyclerToFragment)
        recycler.adapter = adapter

        animateClass.scale(progressBar, 1.0f)
    }

    fun changeMovieTypeFromFragment(movieType: String) {

        userViewModel.changeMovieType(movieType)
        getPage()
    }

    fun setLayoutManager(type: Int){

        var oldScrollPos = (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        when (type){
            1 -> recycler.layoutManager = linearLayoutManager
            2 -> recycler.layoutManager = gridLayoutManager
        }
        adapter.type = type
        recycler.adapter = adapter
        animateClass.recycler(recycler)
        recycler.scrollToPosition(oldScrollPos)
    }

    fun defineType(layoutManager: RecyclerView.LayoutManager?): Int{

        when (layoutManager) {
            linearLayoutManager -> return 1
            else -> return 2
        }
    }

    override fun onAttach(activity: Activity) {
        callBackFromFragmentToActivity = activity as CallBackFromFragmentToActivity
        super.onAttach(activity)
    }

    override fun onMovieClick(position: Int) {
        callBackFromFragmentToActivity.openMovie(allContent.get(position))
    }

    override fun onFavoriteClick(position: Int) {
        Toast.makeText(context,position.toString(), Toast.LENGTH_LONG).show()
    }
}
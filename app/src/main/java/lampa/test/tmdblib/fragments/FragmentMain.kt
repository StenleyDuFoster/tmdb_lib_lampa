package lampa.test.tmdblib.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_main.view.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.api.Results
import lampa.test.tmdblib.fragments.callback.CallBackFromFragmentToActivity
import lampa.test.tmdblib.recycler.RecyclerAdapter
import lampa.test.tmdblib.recycler.callback.CallBackFromRecyclerToFragment


class FragmentMain : Fragment(), CallBackFromRecyclerToFragment {

    lateinit var recycler: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapter: RecyclerAdapter
    lateinit var nestedScroll:NestedScrollView

    lateinit var allContent: ArrayList<Results>

    lateinit var callBackFromFragmentToActivity: CallBackFromFragmentToActivity
    lateinit var onScrollChangeListene:View.OnScrollChangeListener

    var isDownload:Boolean = false

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_main, null)
        recycler = v.findViewById(R.id.recycler)

        linearLayoutManager = LinearLayoutManager(v.context,LinearLayoutManager.VERTICAL,false)
        gridLayoutManager = GridLayoutManager(context,3)
        recycler.layoutManager = linearLayoutManager
        nestedScroll = activity!!.findViewById(R.id.nestedScrollView)

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.w("200",((recycler.layoutManager as LinearLayoutManager).height / 2).toString()
                        +" " + (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition().toString())
                if (((recycler.layoutManager as LinearLayoutManager).height / 2 <
                            (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()) &&
                    !isDownload) {
                    isDownload = true
                    val runnableCode = object: Runnable {
                        override fun run() {
                            callBackFromFragmentToActivity.addMovieToList()
                        }
                    }
                    runnableCode.run()
                }
                Log.w("200",((recycler.layoutManager as LinearLayoutManager).height / 2).toString()
                        +" " + (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition().toString())

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.w("200","onScrollStateChanged")
            }
        })

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            onScrollChangeListene = View.OnScrollChangeListener { view, i, i2, i3, i4 ->
//                if (((recycler.layoutManager as LinearLayoutManager).height / 2 < i4) &&
//                    !isDownload) {
//                    isDownload = true
//                    val runnableCode = object: Runnable {
//                        override fun run() {
//                            callBackFromFragmentToActivity.addMovieToList()
//                        }
//                    }
//                    runnableCode.run()
//                }
//                Log.w("200",((recycler.layoutManager as LinearLayoutManager).height / 2).toString()
//                +" " + i4.toString())
//            }
//            nestedScroll.setOnScrollChangeListener(onScrollChangeListene)
//        }

        return v
    }

    fun setContent(res: List<Results>?){
        nestedScroll.smoothScrollTo(0,0)
        val result_array = ArrayList(res)
        allContent = result_array
        adapter = RecyclerAdapter(result_array, 1, this as CallBackFromRecyclerToFragment)
        recycler.adapter = adapter
        AnimateRecycler()
    }

    fun addContent(res: List<Results>?){
        val result_array = ArrayList(res)
        allContent.addAll(result_array)
        recycler.adapter = adapter
    }

    fun setLayoutManager(type: Int){

        when (type){
            1 -> recycler.layoutManager = linearLayoutManager
            2 -> recycler.layoutManager = gridLayoutManager
        }
        adapter.type = type
        recycler.adapter = adapter
        AnimateRecycler()
    }

    fun AnimateRecycler(){

        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.rec_in)
        recycler.setLayoutAnimation(controller)
        recycler.getAdapter()?.notifyDataSetChanged()
        recycler.scheduleLayoutAnimation()
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        callBackFromFragmentToActivity = activity as CallBackFromFragmentToActivity
    }

    override fun onMovieClick(position: Int) {

        callBackFromFragmentToActivity.openMovie(allContent.get(position))
    }

    override fun onFavoriteClick(position: Int) {
        Toast.makeText(context,position.toString(),Toast.LENGTH_LONG).show()
    }
}
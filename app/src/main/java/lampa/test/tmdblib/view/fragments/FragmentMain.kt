package lampa.test.tmdblib.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.data.Results
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.fragments.callback.CallBackFromFragmentToActivity
import lampa.test.tmdblib.presenter.MainPresenter
import lampa.test.tmdblib.view.anim.Animate
import lampa.test.tmdblib.view.recycler.RecyclerAdapter
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment

class FragmentMain : Fragment(), CallBackFromRecyclerToFragment, MainContract.View {

    lateinit var mPresenter: MainPresenter

    lateinit var recycler: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapter: RecyclerAdapter

    lateinit var callBackFromFragmentToActivity: CallBackFromFragmentToActivity

    lateinit var allContent: ArrayList<Results>

    var isDownload:Boolean = false

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        mPresenter = MainPresenter(this)

        val v: View = inflater.inflate(R.layout.fragment_main, null)
        recycler = v.findViewById(R.id.recycler)

        linearLayoutManager = LinearLayoutManager(v.context,LinearLayoutManager.VERTICAL,false)
        gridLayoutManager = GridLayoutManager(context,3)
        recycler.layoutManager = linearLayoutManager

        getPage()

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (((recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() > recycler.adapter?.itemCount!!/2) &&
                    !isDownload) {
                    isDownload = true
                    val runnableCode = object: Runnable {
                        override fun run() {
                            mPresenter.addPage()
                        }
                    }
                    runnableCode.run()
                }
            }
        })

        return v
    }

    fun getPage(){
        mPresenter.getPage()
    }

    fun setLayoutManager(type: Int){

        var oldScrollPos = (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        when (type){
            1 -> recycler.layoutManager = linearLayoutManager
            2 -> recycler.layoutManager = gridLayoutManager
        }
        adapter.type = type
        recycler.adapter = adapter
        Animate().recycler(recycler)
        recycler.scrollToPosition(oldScrollPos)
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

    override fun showPage(res: List<Results>) {
        val result_array = ArrayList(res)
        allContent = result_array
        adapter = RecyclerAdapter(result_array, 1, this as CallBackFromRecyclerToFragment)
        recycler.adapter = adapter
        Animate().recycler(recycler)
    }

    override fun addToShow(res: List<Results>) {

        val result_array = ArrayList(res)
        allContent.addAll(result_array)


        recycler.adapter?.notifyItemRangeInserted(recycler.adapter!!.itemCount,
            recycler.adapter!!.itemCount+20)
        isDownload = false
    }
}
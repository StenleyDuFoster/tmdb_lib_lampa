package lampa.test.tmdblib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lampa.test.tmdblib.R
import lampa.test.tmdblib.api.Results
import lampa.test.tmdblib.fragments.callback.CallBackFromFragmentToActivity
import lampa.test.tmdblib.recycler.RecyclerAdapter


class FragmentMain(type:Int) : Fragment() {

    lateinit var recycler: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapter: RecyclerAdapter

    lateinit var callBackFromFragmentToActivity: CallBackFromFragmentToActivity

    var type:Int = type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    fun setContent(res: List<Results>?){
        val result_array = ArrayList(res)
        adapter = RecyclerAdapter(result_array, type)
        recycler.adapter = adapter

        adapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {

            override fun onMovieClick(position: Int) {
                callBackFromFragmentToActivity.openMovie(position)
            }

            override fun onFavoriteClick(position: Int) {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_main, null)
        recycler = v.findViewById(R.id.recycler)

        when(type){
            1 ->  {
                linearLayoutManager = LinearLayoutManager(v.context,LinearLayoutManager.VERTICAL,false)
                recycler.layoutManager = linearLayoutManager
            }
            else -> {
                gridLayoutManager = GridLayoutManager(v.context,3)
                recycler.layoutManager = gridLayoutManager
            }
        }

        return v
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is CallBackFromFragmentToActivity) callBackFromFragmentToActivity = (activity as CallBackFromFragmentToActivity?)!!
    }
}
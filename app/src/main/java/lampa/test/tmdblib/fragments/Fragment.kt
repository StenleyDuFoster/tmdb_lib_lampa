package lampa.test.tmdblib.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lampa.test.tmdblib.R
import lampa.test.tmdblib.api.Results
import lampa.test.tmdblib.recycler.RecyclerAdapter

class Fragment(type:Int) : Fragment() {

    lateinit var recycler: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapter: RecyclerAdapter

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_linear, null)
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
}
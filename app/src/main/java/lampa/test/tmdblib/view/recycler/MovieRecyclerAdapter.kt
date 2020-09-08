package lampa.test.tmdblib.view.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment
import lampa.test.tmdblib.view.recycler.holder.GridMovieHolder
import lampa.test.tmdblib.view.recycler.holder.LinearMovieHolder

class MovieRecyclerAdapter :
    RecyclerView.Adapter<MovieRecyclerAdapter.BindHolder>() {

    var mExampleList: ArrayList<MovieResultsTmdbData> = arrayListOf()
    var mListener: CallBackFromRecyclerToFragment? = null

    companion object {
        val LINEAR = 2
        val GRID = 1
    }

    var itemType: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindHolder {

        when (viewType) {
            LINEAR -> {
                val v: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.card_linear, parent, false)
                return LinearMovieHolder(v, mListener)
            }
            else -> {
                val v: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.card_grid, parent, false)
                return GridMovieHolder(v, mListener)
            }
        }
    }

    override fun onBindViewHolder(
        holder: BindHolder,
        position: Int
    ) {
        val currentItem: MovieResultsTmdbData = mExampleList[position]

       holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return mExampleList.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemType
    }

    abstract class BindHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(currentItem: MovieResultsTmdbData)
    }
}
package lampa.test.tmdblib.view.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.util.anim.CustomAnimate
import lampa.test.tmdblib.util.glide.GlideApp
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment

class MovieRecyclerAdapter(exampleList: ArrayList<MovieResultsTmdbData>, var type: Int, listener: CallBackFromRecyclerToFragment?) :
    RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder>() {

    private val mExampleList: ArrayList<MovieResultsTmdbData> = exampleList
    private var mListener: CallBackFromRecyclerToFragment? = listener

    inner class ViewHolder(itemView: View, listener: CallBackFromRecyclerToFragment?) :
        RecyclerView.ViewHolder(itemView) {
        var textMain: TextView = itemView.findViewById(R.id.card_main_text)
        var textSlave: TextView = itemView.findViewById(R.id.card_slave_text)
        var imageView: ImageView = itemView.findViewById(R.id.card_image)
        private var imageFavorite: ImageView = itemView.findViewById(R.id.image_favorite)
        lateinit var textContent: TextView

        init {

            if(getItemViewType(adapterPosition) == 1)
                textContent = itemView.findViewById(R.id.card_content_text)

            imageFavorite.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onFavoriteClick(position)
                        CustomAnimate.alphaBlink(imageFavorite)
                    }
                }
            }

            itemView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onMovieClick(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val lay: Int = when (viewType) {
            1 -> {
                R.layout.card_linear
            }
            else -> {
                R.layout.card_grid
            }
        }

        val v: View = LayoutInflater.from(parent.context).inflate(lay, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem: MovieResultsTmdbData = mExampleList[position]

        if (currentItem.title.length > 20) {
            holder.textMain.text = (currentItem.title.substring(0, 20) + " ...")
        } else {
            holder.textMain.text = currentItem.title
        }

        holder.textSlave.text = currentItem.release_date

        GlideApp.with(holder.imageView.context)
             .load("https://image.tmdb.org/t/p/w500" + currentItem.poster_path)
             .into(holder.imageView)
        
        when(holder.itemViewType){
            1 -> {
                val overview = currentItem.overview
                if(overview.length > 100) {
                    holder.textContent.text = (overview.substring(0, 100) + " ...")
                } else {
                    holder.textContent.text = overview
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mExampleList.size
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }
}
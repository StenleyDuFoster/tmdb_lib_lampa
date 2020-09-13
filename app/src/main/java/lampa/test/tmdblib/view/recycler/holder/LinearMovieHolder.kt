package lampa.test.tmdblib.view.recycler.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.util.anim.CustomAnimate
import lampa.test.tmdblib.util.glide.GlideApp
import lampa.test.tmdblib.view.recycler.MovieRecyclerAdapter
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment

class LinearMovieHolder(itemView: View, listener: CallBackFromRecyclerToFragment?) :
    MovieRecyclerAdapter.BindHolder(itemView) {
    var textMain: TextView = itemView.findViewById(R.id.card_main_text)
    var textSlave: TextView = itemView.findViewById(R.id.card_slave_text)
    var imageView: ImageView = itemView.findViewById(R.id.card_image)
    var textContent: TextView = itemView.findViewById(R.id.card_content_text)
    private var imageFavorite: ImageView = itemView.findViewById(R.id.image_favorite)

    init {

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

    override fun bind(currentItem: MovieResultsTmdbData) {
        if (currentItem.title.length > 20) {
            textMain.text = (currentItem.title.substring(0, 20) + " ...")
        } else {
            textMain.text = currentItem.title
        }

        textSlave.text = currentItem.release_date

        GlideApp.with(imageView.context)
            .load("https://image.tmdb.org/t/p/w500" + currentItem.poster_path)
            .into(imageView)

        val overview = currentItem.overview
        if (overview.length > 100) {
            textContent.text = (overview.substring(0, 100) + " ...")
        } else {
            textContent.text = overview
        }
    }
}
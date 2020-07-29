package lampa.test.tmdblib.view.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.data.Results
import lampa.test.tmdblib.view.recycler.callback.CallBackFromRecyclerToFragment


class RecyclerAdapter(exampleList: ArrayList<Results>, type: Int, listener: CallBackFromRecyclerToFragment?) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val mExampleList: ArrayList<Results>
    private var mListener: CallBackFromRecyclerToFragment? = listener

    var type = type

    inner class ViewHolder(itemView: View, listener: CallBackFromRecyclerToFragment?) :
        RecyclerView.ViewHolder(itemView) {
        var textMain: TextView
        var textSlave: TextView
        var imageView: ImageView
        var imageFavorite: ImageView
        lateinit var textContent: TextView

        init {
            textMain = itemView.findViewById(R.id.card_main_text)
            textSlave = itemView.findViewById(R.id.card_slave_text)
            imageView = itemView.findViewById(R.id.card_image)
            imageFavorite = itemView.findViewById(R.id.image_favorite)

            if(getItemViewType(position)==1)
                textContent = itemView.findViewById(R.id.card_content_text)

            imageFavorite.setOnClickListener({ v ->
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onFavoriteClick(position)
                    }
                }
            })

            itemView.setOnClickListener({ v ->
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onMovieClick(position)
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val lay: Int
        when (viewType) {
            1 -> {
                lay = R.layout.card_linear
            }
            else -> {
                lay = R.layout.card_grid
            }
        }

        val v: View = LayoutInflater.from(parent.context).inflate(lay, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem: Results = mExampleList[position]

        if (currentItem.title.length > 20)
            holder.textMain.setText(currentItem.title.substring(0,20) + " ...")
        else
            holder.textMain.setText(currentItem.title)

        holder.textSlave.setText(currentItem.release_date)

        Glide.with(holder.imageView.context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w500" + currentItem.poster_path)
            .into(holder.imageView)


        when(holder.itemViewType){
            1 -> {
                val overview = currentItem.overview
                if(overview.length > 100)
                    holder.textContent.setText(overview.substring(0,100) + " ...")
                else
                    holder.textContent.setText(overview)
            }
        }
    }

    override fun getItemCount(): Int {
        return mExampleList.size
    }

    init {
        mExampleList = exampleList
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }
}
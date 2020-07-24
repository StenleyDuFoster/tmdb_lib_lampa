package lampa.test.tmdblib.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lampa.test.tmdblib.R
import lampa.test.tmdblib.api.Results


class RecyclerAdapter(exampleList: ArrayList<Results>, type:Int) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val mExampleList: ArrayList<Results>
    private var mListener: OnItemClickListener? = null

    val type = type

    interface OnItemClickListener {
        fun onMovieClick(position: Int)
        fun onFavoriteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    inner class ViewHolder(itemView: View, listener: OnItemClickListener?, type:Int) :
        RecyclerView.ViewHolder(itemView) {
        var textMain: TextView
        var textSlave: TextView
        var imageView: ImageView
        lateinit var textContent: TextView

        init {
            textMain = itemView.findViewById(R.id.card_main_text)
            textSlave = itemView.findViewById(R.id.card_slave_text)
            imageView = itemView.findViewById(R.id.card_image)

            if(type==1)
                textContent = itemView.findViewById(R.id.card_content_text)

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
        when (type) {
            1 -> {
                lay = R.layout.card_linear
            }
            else -> {
                lay = R.layout.card_grid
            }
        }

        val v: View = LayoutInflater.from(parent.context).inflate(lay, parent, false)
        return ViewHolder(v, mListener, type)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem: Results = mExampleList[position]

        if (currentItem.title.length > 35)
            holder.textMain.setText(currentItem.title.substring(0,35) + " ...")
        else
            holder.textMain.setText(currentItem.title)

        holder.textSlave.setText(currentItem.release_date)

        Glide.with(holder.imageView.context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w500" + currentItem.poster_path)
            .into(holder.imageView)


        when(type){
            1 -> {
                val overview = currentItem.overview
                if(overview.length>150)
                    holder.textContent.setText(overview.substring(0,150) + " ...")
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
}
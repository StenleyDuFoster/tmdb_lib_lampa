package lampa.test.tmdblib.recycler

import android.util.Log
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

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener?, type:Int) :
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
                        listener.onItemClick(position)
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
        Log.v("200","sozdal")
        when (type) {
            1 -> {
                lay = R.layout.card_linear
            }
            else -> {
                lay = R.layout.card_grid
            }
        }
        val v: View = LayoutInflater.from(parent.context).inflate(lay, parent, false)
        return ViewHolder(v, mListener,type)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem: Results = mExampleList[position]

        holder.textMain.setText(currentItem.name)
        holder.textSlave.setText(currentItem.first_air_date)

        Glide.with(holder.imageView.context)
            .load("https://image.tmdb.org/t/p/w500"+currentItem.poster_path)
            .into(holder.imageView)

        when(type){
            1 -> {
                holder.textContent.setText(currentItem.overview)
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
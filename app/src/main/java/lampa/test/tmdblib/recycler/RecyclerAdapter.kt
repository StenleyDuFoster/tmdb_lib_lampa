package lampa.test.tmdblib.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lampa.test.tmdblib.R

class RecyclerAdapter(exampleList: ArrayList<CardScript>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val mExampleList: ArrayList<CardScript>
    private var mListener: OnItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
//        var textView: TextView
//        var imageView: ImageView

        init {
//            textView = itemView.findViewById(R.id.text_card)
//            imageView = itemView.findViewById(R.id.image_card)
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
        when (viewType) {
            1 -> lay = R.layout.card_linear
            else -> lay = R.layout.card_grid
        }
        val v: View = LayoutInflater.from(parent.context).inflate(lay, parent, false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem: CardScript = mExampleList[position]

//        holder.textView.setText(currentItem.getText())
//        holder.imageView.setBackground(currentItem.getImage())
    }

    override fun getItemCount(): Int {
        return mExampleList.size
    }

    init {
        mExampleList = exampleList
    }
}
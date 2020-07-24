package lampa.test.tmdblib.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import lampa.test.tmdblib.R
import lampa.test.tmdblib.api.Results

class FragmentDetails : Fragment() {

    lateinit var tv_title:TextView
    lateinit var tv_genre:TextView
    lateinit var tv_content:TextView

    lateinit var iv_details:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_details, container, false)

        tv_title = v.findViewById(R.id.title_text)
        tv_genre = v.findViewById(R.id.genre_text)
        tv_content = v.findViewById(R.id.content_text)

        iv_details = v.findViewById(R.id.image_details)

        return v
    }

    fun setContent(res: Results){

        tv_title.setText(res.title)
        tv_genre.setText(res.genre_ids.toString() + res.release_date)
        tv_content.setText(res.overview + " " + res.original_name +
        " " + res.release_date)

        Glide.with(tv_title.context)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w500" + res.poster_path)
            .into(iv_details)
    }
}
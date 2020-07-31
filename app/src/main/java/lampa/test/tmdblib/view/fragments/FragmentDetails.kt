package lampa.test.tmdblib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

import androidx.fragment.app.Fragment

import com.bumptech.glide.Glide

import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.data.Results

class FragmentDetails : Fragment() {

    private lateinit var tv_title:TextView
    private lateinit var tv_genre:TextView
    private lateinit var tv_content:TextView

    private lateinit var rate_view:RatingBar

    private lateinit var iv_details:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_details, container, false)

        tv_title = v.findViewById(R.id.title_text)
        tv_genre = v.findViewById(R.id.genre_text)
        tv_content = v.findViewById(R.id.content_text)

        rate_view = v.findViewById(R.id.ratingBar)

        iv_details = v.findViewById(R.id.image_details)

        return v
    }

    fun setContent(res: Results){

        tv_title.setText(res.title)
        tv_genre.setText(res.release_date)
        tv_content.setText("  " + res.overview)

        rate_view.rating = res.vote_average

        Glide.with(activity?.applicationContext!!)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w500" + res.poster_path)
            .into(iv_details)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setDisplayShowHomeEnabled(true)
        activity?.actionBar?.setTitle(res.title)
    }
}
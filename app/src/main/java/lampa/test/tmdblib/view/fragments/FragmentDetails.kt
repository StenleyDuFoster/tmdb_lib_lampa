package lampa.test.tmdblib.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_details.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.repository.data.MovieResultsTmdbData

class FragmentDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_details, container, false)
    }

    fun setContent(res: MovieResultsTmdbData){

        titleText.text = res.title
        genreText.text = res.release_date
        contentText.text =  res.overview
        ratingBar.rating = res.vote_average

        Glide.with(activity?.applicationContext!!)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w500" + res.poster_path)
            .into(imageDetails)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.title = res.title
    }
}
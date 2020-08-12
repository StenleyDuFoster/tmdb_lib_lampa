package lampa.test.tmdblib.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.fragment_details.*

import lampa.test.tmdblib.R
import lampa.test.tmdblib.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.view.fragments.base.BaseFragment

class FragmentDetails : BaseFragment(R.layout.fragment_details) {

    var content: MovieResultsTmdbData? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(content != null) {
            titleText.text = content?.title
            genreText.text = content?.release_date
            contentText.text = content?.overview
            ratingBar.rating = content?.vote_average!!

            Glide.with(activity?.applicationContext!!)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500" + content?.poster_path)
                .into(imageDetails)

            activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
            activity?.actionBar?.title = content?.title
            super.onViewCreated(view, savedInstanceState)
        }
    }
}
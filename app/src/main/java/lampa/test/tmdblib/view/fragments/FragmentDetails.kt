package lampa.test.tmdblib.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_details.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.view.activity.VideoActivity
import lampa.test.tmdblib.view.fragments.base.BaseFragment
import java.util.concurrent.TimeUnit

class FragmentDetails : BaseFragment(R.layout.fragment_details) {

    var content: MovieResultsTmdbData? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (savedInstanceState?.get("content") != null && content == null) {
            content = savedInstanceState.getParcelable("content")!!
        }

        if (content != null) {
            titleText.text = content?.title
            genreText.text = content?.release_date
            contentText.text = content?.overview
            ratingBar.rating = content?.vote_average!!

            compositeDisposable.add(
                buttonVideo.clicks()
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        val intent = Intent(context, VideoActivity::class.java)
                        intent.putExtra("content", content)
                        startActivity(intent)
                    }
            )

            Glide.with(activity?.applicationContext!!)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500" + content?.poster_path)
                .into(imageDetails)

            activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
            activity?.actionBar?.title = content?.title
            super.onViewCreated(view, savedInstanceState)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("content", content)
    }

    override fun onDestroyView() {

        compositeDisposable.clear()
        super.onDestroyView()
    }
}
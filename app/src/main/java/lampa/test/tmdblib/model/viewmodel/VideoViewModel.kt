package lampa.test.tmdblib.model.viewmodel

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VideoViewModel : ViewModel() {

    val liveData = MutableLiveData<String>()
    val liveError = MutableLiveData<String>()
    val compositeDisposable = CompositeDisposable()

    lateinit var doc: Document

    fun getFilmUrl(title: String, date: String) {

        val filmTasker = FilmTasker()
        filmTasker.execute()

    }

    inner class FilmTasker : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg p0: Void?): Void? {

            doc = Jsoup
                    .connect("https://filmix.co/serials/59953-rino-911-reno-911-serial-2003-2009.html")
                    .get()

            if (doc != null) {
                Log.v(
                    "112233",
                    doc.body()
                        .getElementById("player")
                        .toString()
                )
            }
            return null
        }

    }

    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onCleared()
    }
}
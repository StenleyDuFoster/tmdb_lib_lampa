package lampa.test.tmdblib.model.viewmodel.repository.internet.parser

import android.util.Log
import android.webkit.JavascriptInterface
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackPageFromParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JavaScriptParserPage(private val callBack: CallBackPageFromParser) {

    lateinit var doc: Document

    @JavascriptInterface
    fun processHTML(html: String?) {

        doc = Jsoup
            .parse(html)

        Log.v("112233", "start parse")

        if (doc != null) {

            val parsePage = doc
                .body()
                .getElementsByClass("watch icon-play")

            if (parsePage.size > 4) {
                Log.v("112233", "page $parsePage")
                val parse = parsePage[0].toString().split("\"")
                Log.v("112233", "page " + parse[5])
                callBack.onPageFind(parse[5])
            } else {
                if(!parsePage.isNullOrEmpty()) {
                    val parse = parsePage[0].toString().split("\"")
                    Log.v("112233", "page " + parse[5])
                    callBack.onPageFind(parse[5])
                } else {
                    callBack.onPageNotFound()
                }
            }
        }
    }
}

package lampa.test.tmdblib.repository.internet.parser

import android.util.Log
import android.webkit.JavascriptInterface
import lampa.test.tmdblib.repository.internet.parser.callBack.CallBackPageFromParser
import lampa.test.tmdblib.repository.internet.parser.callBack.CallBackVideoFromParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JavaScriptParserPage(private val callBack: CallBackPageFromParser) {

    lateinit var doc: Document

    @JavascriptInterface
    fun processHTML(html: String?) {

        doc = Jsoup
            .parse(html)

        if (doc != null) {

            val parsePage = doc
                .body()
                .getElementsByClass("watch icon-play")

            if(parsePage.size > 4) {
                Log.v("112233", "page" + parsePage)
                Log.v("112233", "page" + parsePage[4])
                val parse = parsePage[0].toString()
                callBack.onPageFind(parse)
            } else {
                val parse = parsePage[0].toString().split("\"")
                callBack.onPageFind(parse[5])
            }
        }
    }
}

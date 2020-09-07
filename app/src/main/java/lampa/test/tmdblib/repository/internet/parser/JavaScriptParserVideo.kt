package lampa.test.tmdblib.repository.internet.parser

import android.util.Log
import android.webkit.JavascriptInterface
import lampa.test.tmdblib.repository.internet.parser.callBack.CallBackVideoFromParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JavaScriptParserVideo(private val callBack: CallBackVideoFromParser) {

    lateinit var doc: Document

    @JavascriptInterface
    fun processHTML(html: String?) {

        doc = Jsoup
            .parse(html)

        if (doc != null) {

            val parseArray = doc
                .body()
                .getElementById("oframeplayer")
                .getElementsByAttribute("src")
                .toString()
                .split(" \" ")

            Log.v("112233", "video" + parseArray.toString())

            if(parseArray.size < 4) {
                val parse = parseArray[0].toString().split("\"")
                callBack.onVideoFind(parse[1])
            }

        }
    }
}

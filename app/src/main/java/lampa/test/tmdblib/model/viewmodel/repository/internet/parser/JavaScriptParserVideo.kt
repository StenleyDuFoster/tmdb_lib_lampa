package lampa.test.tmdblib.model.viewmodel.repository.internet.parser

import android.util.Log
import android.webkit.JavascriptInterface
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackVideoFromParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JavaScriptParserVideo(private val callBack: CallBackVideoFromParser) {

    private lateinit var doc: Document

    @JavascriptInterface
    fun processHTML(html: String?) {

        doc = Jsoup
            .parse(html)

        Log.v("112233", "video" + " start")

        if (doc != null) {

            val parseArray = doc
                .body()
                .getElementById("oframeplayer").child(2).child(0).getElementsByAttribute("src").toString().split(" \" ")

            Log.v("112233", "video $parseArray")

            if(parseArray.size < 4) {
                Log.v("112233", "call back")
                val parse = parseArray[0].split("\"")
                callBack.onVideoFind(parse[3])
            }

        }
    }
}

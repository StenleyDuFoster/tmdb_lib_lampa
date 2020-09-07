package lampa.test.tmdblib.view.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_video.*
import lampa.test.tmdblib.R
import lampa.test.tmdblib.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.repository.internet.parser.JavaScriptParserPage
import lampa.test.tmdblib.repository.internet.parser.JavaScriptParserVideo
import lampa.test.tmdblib.repository.internet.parser.callBack.CallBackPageFromParser
import lampa.test.tmdblib.repository.internet.parser.callBack.CallBackVideoFromParser
import lampa.test.tmdblib.view.activity.base.BaseActivity


class VideoActivity : BaseActivity(), CallBackVideoFromParser, CallBackPageFromParser {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        (intent.extras?.get("content") as MovieResultsTmdbData).let {
            findPageUtlWithVideoView(it)
        }
    }

    @JavascriptInterface
    fun findPageUtlWithVideoView(content: MovieResultsTmdbData) {

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JavaScriptParserPage(this), "PAGE")
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:window.PAGE.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
            }
        })
        webView.getSettings().setUserAgentString("Chrome/41.0.2228.0 Safari/537.36")

        Log.v("112233", "load " + "https://filmix.co/search/" + content.title + "-" + content.release_date.substring(0,4))
        webView.loadUrl("https://filmix.co/search/" + content.title + "-" + content.release_date.substring(0,4))
    }//+ content.title + "-" + content.release_date

    override fun onPageFind(link: String) {
        findVideoUrlWithVideoView(link)
    }

    @JavascriptInterface
    fun findVideoUrlWithVideoView(pageUrl: String) {

        runOnUiThread {
            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(JavaScriptParserVideo(this), "VIDEO")
            webView.setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    webView.loadUrl("javascript:window.VIDEO.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                }
            })
            webView.getSettings().setUserAgentString("Chrome/41.0.2228.0 Safari/537.36")

            webView.loadUrl(pageUrl)
        }
    }

    override fun onVideoFind(link: String) {

        runOnUiThread {
            videoView.setVideoURI(Uri.parse(link))
            val mediaController = MediaController(this)
            videoView.setMediaController(mediaController)
            mediaController.setAnchorView(videoView)
        }
    }
}
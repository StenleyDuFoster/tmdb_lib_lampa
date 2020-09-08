package lampa.test.tmdblib.view.activity

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.MediaController
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.loading_lay.*
import kotlinx.coroutines.Runnable
import lampa.test.tmdblib.R
import lampa.test.tmdblib.model.viewmodel.repository.data.MovieResultsTmdbData
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.JavaScriptParserPage
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.JavaScriptParserVideo
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackPageFromParser
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackVideoFromParser
import lampa.test.tmdblib.util.toast.toast
import lampa.test.tmdblib.view.activity.base.BaseActivity

class VideoActivity : BaseActivity(), CallBackVideoFromParser, CallBackPageFromParser {

    val refreshHandler = Handler()
    var runnableRefresh: Runnable? = null
    val delayHandler = Handler()
    var runnableDelay: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_video)
        findPageUrlWithVideoView((intent.extras?.get("content") as MovieResultsTmdbData))
    }

    @JavascriptInterface
    fun findPageUrlWithVideoView(content: MovieResultsTmdbData) {

        textLoading.text = "ищем страницу " + content.title
        runOnUiThread {
            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(JavaScriptParserPage(this), "PAGE")
            webView.setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {

                    runnableDelay = Runnable {
                        webView.loadUrl("javascript:window.PAGE.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                        textLoading.text = "парсинг страницы"
                    }
                    delayHandler.postDelayed(
                        runnableDelay!!, 5000
                    )
                }
            })
            webView.getSettings().setUserAgentString("Chrome/41.0.2228.0 Safari/537.36")

            webView.loadUrl(
                "https://filmix.co/search/" +
                        content.title +
                        "-" +
                        content.release_date.substring(0, 4)
            )
        }
    }

    override fun onPageFind(link: String) {
        findVideoUrlWithVideoView(link)
    }

    override fun onPageNotFound() {
        runnableRefresh = Runnable {

            runOnUiThread {
                textLoading.text = "ошибка 101, ожидайте"
                findPageUrlWithVideoView((intent.extras?.get("content") as MovieResultsTmdbData))
                textLoading.text = "перезагрузка страницы"
            }

        }

        refreshHandler.postDelayed(
            runnableRefresh!!, 2000
        )
    }

    @JavascriptInterface
    fun findVideoUrlWithVideoView(pageUrl: String) {
        if(runnableDelay != null) {
            delayHandler.removeCallbacks(runnableDelay!!)
        }
        if(runnableRefresh != null) {
            refreshHandler.removeCallbacks(runnableRefresh!!)
        }
        runOnUiThread {
            textLoading.text = "поиск видео"
            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(JavaScriptParserVideo(this), "VIDEO")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    webView.loadUrl("javascript:window.VIDEO.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                }
            }
            webView.settings.userAgentString = "Chrome/41.0.2228.0 Safari/537.36"

            webView.loadUrl(pageUrl)
        }
    }

    override fun onVideoFind(link: String) {

        runOnUiThread {
            textLoading.text = "запуск видео"
            videoView.setVideoURI(Uri.parse(link))
            val mediaController = MediaController(this)
            videoView.setMediaController(mediaController)
            mediaController.setAnchorView(videoView)
            videoView.start()
            layLoading.alpha = 0f
        }
    }

    override fun onDestroy() {
        webView.clearHistory()
        webView.clearMatches()
        webView.clearSslPreferences()
        if(runnableDelay != null) {
            delayHandler.removeCallbacks(runnableDelay!!)
        }
        if(runnableRefresh != null) {
            refreshHandler.removeCallbacks(runnableRefresh!!)
        }
        clearFindViewByIdCache()
        super.onDestroy()
    }
}
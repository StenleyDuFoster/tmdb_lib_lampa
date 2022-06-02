package lampa.test.tmdblib.view.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import lampa.test.tmdblib.view.activity.base.BaseActivity

class VideoActivity : BaseActivity(), CallBackVideoFromParser, CallBackPageFromParser {

    private val refreshHandler = Handler(Looper.getMainLooper())
    private val delayHandler = Handler(Looper.getMainLooper())
    private var runnableRefresh: Runnable? = null
    private var runnableDelay: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupWindow()
        setContentView(R.layout.activity_video)
        (intent.extras?.get("content") as? MovieResultsTmdbData?)?.let {
            findPageUrlWithVideoView(it)
        }
    }

    private fun setupWindow() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    @JavascriptInterface
    fun findPageUrlWithVideoView(content: MovieResultsTmdbData) {

        textLoading.text = ("ищем страницу " + content.title)
        runOnUiThread {
            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(JavaScriptParserPage(this), "PAGE")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    runnableDelay = Runnable {
                        webView.loadUrl("javascript:window.PAGE.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                        textLoading.text = "парсинг страницы"
                    }
                    delayHandler.postDelayed(
                        runnableDelay!!, 5000
                    )
                }
            }
            webView.settings.userAgentString = "Chrome/41.0.2228.0 Safari/537.36"

            Log.v("112233", "https://filmix.ac/search/" +
                    content.title +
                    "-" +
                    content.release_date.substring(0, 4))

            webView.loadUrl(
                "https://filmix.ac/search/" +
                        content.title +
                        "-" +
                        content.release_date.substring(0, 4)
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
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
                    Handler(Looper.getMainLooper()).postDelayed({
                        webView.loadUrl("javascript:window.VIDEO.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                    }, 3000)
                }
            }
            webView.settings.userAgentString = "Chrome/41.0.2228.0 Safari/537.36"

            webView.loadUrl(pageUrl)
        }
    }

    override fun onPageFind(link: String) {
        findVideoUrlWithVideoView(link)
    }

    override fun onPageNotFound() {
        runnableRefresh = Runnable {

            runOnUiThread {
                textLoading.text = ("ошибка 101, ожидайте")
                findPageUrlWithVideoView(intent.extras?.get("content") as MovieResultsTmdbData)
                textLoading.text = "перезагрузка страницы"
            }

        }

        refreshHandler.postDelayed(
            runnableRefresh!!, 2000
        )
    }

    override fun onVideoFind(link: String) {

        runOnUiThread {
            webView.stopLoading()
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
        webView.stopLoading()
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
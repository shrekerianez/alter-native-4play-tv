package app.alternative.fourplay.tv

import android.app.Activity
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : Activity() {

    companion object {
        private const val TV_URL = "https://shrekerianez.github.io/alter-native-4play/?tv"
        private const val ALLOWED_HOST = "shrekerianez.github.io"
        private const val ALLOWED_PATH_PREFIX = "/alter-native-4play/"
    }

    private lateinit var root: FrameLayout
    private lateinit var webView: WebView
    private lateinit var overlay: LinearLayout
    private lateinit var overlayTitle: TextView
    private lateinit var overlayMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var progress: ProgressBar
    private lateinit var connectivityManager: ConnectivityManager

    private var pageFailed = false
    private var networkCallbackRegistered = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            runOnUiThread {
                if (pageFailed || webView.url.isNullOrBlank()) {
                    loadTv()
                }
            }
        }

        override fun onLost(network: Network) {
            runOnUiThread {
                if (!isOnline()) {
                    showOffline()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK
        hideSystemUi()

        root = FrameLayout(this).apply {
            setBackgroundColor(Color.rgb(10, 10, 13))
        }

        webView = WebView(this).apply {
            setBackgroundColor(Color.rgb(10, 10, 13))
            setVisibility(View.INVISIBLE)
            isFocusable = true
            isFocusableInTouchMode = true
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                builtInZoomControls = false
                displayZoomControls = false
                setSupportZoom(false)
                mediaPlaybackRequiresUserGesture = true
                userAgentString = "$userAgentString 4PLAY-TV/AndroidTV"
            }
            CookieManager.getInstance().setAcceptCookie(true)
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                    pageFailed = false
                    showLoading()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (!pageFailed) {
                        view?.setVisibility(View.VISIBLE)
                        overlay.setVisibility(View.GONE)
                        view?.requestFocus()
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val uri = request?.url ?: return true
                    val allowed = uri.scheme == "https" &&
                        uri.host.equals(ALLOWED_HOST, ignoreCase = true) &&
                        uri.path.orEmpty().startsWith(ALLOWED_PATH_PREFIX)
                    return !allowed
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: android.webkit.WebResourceError?
                ) {
                    if (request?.isForMainFrame == true) {
                        pageFailed = true
                        showOffline()
                    }
                }
            }
        }

        overlay = buildOverlay()
        root.addView(webView, FrameLayout.LayoutParams(-1, -1))
        root.addView(overlay, FrameLayout.LayoutParams(-1, -1))
        setContentView(root)

        connectivityManager = getSystemService(ConnectivityManager::class.java)
        registerNetworkCallback()
        loadTv()
    }

    private fun buildOverlay(): LinearLayout {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)
            setBackgroundColor(Color.rgb(10, 10, 13))
        }

        val brand = TextView(this).apply {
            text = "4PLAY"
            textSize = 54f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "Party Hub\nby AlterNative"
            textSize = 20f
            setTextColor(Color.rgb(190, 190, 198))
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 36)
        }

        progress = ProgressBar(this).apply {
            isIndeterminate = true
        }

        overlayTitle = TextView(this).apply {
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 28, 0, 8)
        }

        overlayMessage = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.rgb(180, 180, 188))
            gravity = Gravity.CENTER
        }

        retryButton = Button(this).apply {
            text = "Повторить"
            isFocusable = true
            setOnClickListener { loadTv() }
        }

        container.addView(brand)
        container.addView(subtitle)
        container.addView(progress)
        container.addView(overlayTitle)
        container.addView(overlayMessage)
        container.addView(retryButton)
        return container
    }

    private fun showLoading() {
        overlay.setVisibility(View.VISIBLE)
        overlayTitle.text = "Подключаем 4PLAY"
        overlayMessage.text = ""
        progress.setVisibility(View.VISIBLE)
        retryButton.setVisibility(View.GONE)
    }

    private fun showOffline() {
        webView.setVisibility(View.INVISIBLE)
        overlay.setVisibility(View.VISIBLE)
        overlayTitle.text = "Нет подключения к интернету"
        overlayMessage.text = "Проверьте сеть и попробуйте снова"
        progress.setVisibility(View.GONE)
        retryButton.setVisibility(View.VISIBLE)
        retryButton.requestFocus()
    }

    private fun loadTv() {
        hideSystemUi()
        if (!isOnline()) {
            showOffline()
            return
        }
        pageFailed = false
        showLoading()
        webView.loadUrl(TV_URL)
    }

    private fun isOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun registerNetworkCallback() {
        if (networkCallbackRegistered) return
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
        networkCallbackRegistered = true
    }

    private fun hideSystemUi() {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUi()
    }

    @Deprecated("Deprecated in Android API; retained for broad Android TV compatibility")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (networkCallbackRegistered) {
            runCatching { connectivityManager.unregisterNetworkCallback(networkCallback) }
            networkCallbackRegistered = false
        }
        webView.stopLoading()
        webView.destroy()
        super.onDestroy()
    }
}

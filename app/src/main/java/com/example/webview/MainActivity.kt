package com.example.webview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var decorView: View

    @SuppressLint("JavascriptInterface", "AddJavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(this), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }
        webView.loadUrl("https://www.google.com/")
        hideNavigation()
    }

    private fun hideNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    class WebAppInterface(private val context: Context) {
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
        }
    }

    private fun commandLine() {
        //        window.decorView.apply {
//            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or//hide navigation
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or//?
//                    View.SYSTEM_UI_FLAG_FULLSCREEN or//nếu để thì vẫn hiện giờ và pin trên cùng
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or//nếu không có thì load app sẽ ẩn navigation,nhưng khi click sẽ show lại navigation
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or//?
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE//?
//        }

//*******----- tuong tac html vs app----******//
        //        val html="<<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n" +
//                "<script type=\"text/javascript\">\n" +
//                "    function showAndroidToast(toast) {\n" +
//                "        Android.showToast(toast);\n" +
//                "    }\n" +
//                "</script>"
//        webView.loadData(html,"text/html",null)

    }
}
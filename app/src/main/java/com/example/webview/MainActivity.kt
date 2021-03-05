package com.example.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.webview.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.layout_test.*
import java.io.InputStream
import java.lang.Thread.setDefaultUncaughtExceptionHandler


class MainActivity : AppCompatActivity() {
    //lateinit var webView: WebView
    //lateinit var decorView: View

    companion object {
        private const val REQUEST_START = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var kioskUtils: KioskUtils

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultUncaughtExceptionHandler()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        kioskUtils = KioskUtils(this)
        kioskUtils.start(this)

//        binding.kioskOffButton.setOnClickListener {
//            kioskUtils.stop(this)
//
//        }
//        binding.clearDeviceOwnerButton.setOnClickListener {
//            kioskUtils.clearDeviceOwner()
//
//        }
//        btn1.setOnClickListener {
//            this.startLockTask()
//        }
//        btn2.setOnClickListener {
//            this.stopLockTask()
//        }



//        webView = findViewById(R.id.webView)
//        webView.settings.javaScriptEnabled = true
//        webView.addJavascriptInterface(WebAppInterface(this), "Android")
//        webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(url!!)
//                return true
//            }
//        }
//        startService()
//        webView.loadUrl("https://www.google.com/")
       // hideNavigation()
      // window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        kioskUtils.start(this)
    }
    private fun setDefaultUncaughtExceptionHandler() {
        val pendingIntent = PendingIntent.getActivity(
            application.baseContext,
            REQUEST_START,
            Intent(intent),
            PendingIntent.FLAG_CANCEL_CURRENT)
        val origin = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            @Volatile
            private var crashing = false

            override fun uncaughtException(thread: Thread, throwable: Throwable) {
                try {
                    if (crashing) {
                        return
                    }
                    crashing = true
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val triggerAtMillis = System.currentTimeMillis() + 100
                    if (android.os.Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
                    }
                } finally {
                    origin.uncaughtException(thread, throwable)
                }
            }
        })
    }

    private fun startService() {
        val intent = Intent(this, MyService::class.java)
        startService(intent)
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
//        if (webView.canGoBack()) {
//            webView.goBack()
//        } else {
//            super.onBackPressed()
//        }
    }

    class WebAppInterface(private val context: Context) {
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        val intentBroad = Intent(this, MyService::class.java)
        intentBroad.action = "PHUC"
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroad)
        super.onDestroy()
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
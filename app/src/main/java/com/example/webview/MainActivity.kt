package com.example.webview

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.webview.databinding.ActivityMainBinding
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Thread.setDefaultUncaughtExceptionHandler


class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView

    companion object {
        private const val REQUEST_START = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var kioskUtils: KioskUtils

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultUncaughtExceptionHandler()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        kioskUtils = KioskUtils(this)

        binding.btnKioskOff.setOnClickListener {
            // kioskUtils.start(this)
//            Runtime.getRuntime()
//                .exec("adb shell dpm set-device-owner com.example.webview/.AdminReceiver")
//
//            val mStartActivity = Intent(this, MainActivity::class.java)
//            val mPendingIntentId = 123456
//            val mPendingIntent = PendingIntent.getActivity(
//                this,
//                mPendingIntentId,
//                mStartActivity,
//                PendingIntent.FLAG_CANCEL_CURRENT
//            )
//           val alm =  this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            alm.set(AlarmManager.RTC, System.currentTimeMillis() + 300, mPendingIntent)
//            exitProcess(0)

            kioskUtils.stop(this)
            kioskUtils.clearDeviceOwner()
        }
//        val proc = Runtime.getRuntime()
//            .exec(arrayOf("su", "-c", "adb shell dpm set-device-owner com.example.webview/.AdminReceive", "", "exit"))
//        proc.waitFor()
        webView = findViewById(R.id.web_view)
        webView.settings.javaScriptEnabled = true
        // webView.addJavascriptInterface(WebAppInterface(this), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }
        webView.loadUrl("https://www.google.com/")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        kioskUtils.start(this)
        rootTest()
    }

    private fun rootTest() {
        try {
           val p= Runtime.getRuntime().exec("adb shell dpm set-device-owner com.example.webview/.AdminReceiver")
            p.waitFor()
            Log.d("pro","$p")
        }catch (io:IOException){

        }

    }


    private fun setDefaultUncaughtExceptionHandler() {
        val pendingIntent = PendingIntent.getActivity(
            application.baseContext,
            REQUEST_START,
            Intent(intent),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val origin = Thread.getDefaultUncaughtExceptionHandler()
        setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
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
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
                } finally {
                    origin.uncaughtException(thread, throwable)
                }
            }
        })
    }

}

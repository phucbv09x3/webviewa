package com.example.webview

import android.app.*
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.*
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MyService : Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: NotificationCompat.Builder
    private lateinit var pendingIntent: PendingIntent

    companion object {
        const val CHANNEL_ID = "IDMapService"
        const val CHANNEL_NAME = "MapService"
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_TITLE = "Create by Miichisoft"
        const val ACTION_CLOSE = "CLOSE"
        const val DESCRIPTION = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
        const val TIME_DEFAULT = 60 * 60 * 1000L
        const val TEXT_NOTIFY_LOADING = "Đang cập nhật !"
        const val REQUEST_CODE = 1
        const val FLAGS_ZERO = 0
        const val MILLIS_ONE_MINUTE = 60 * 1000L
        const val NAME_FILE_SHARE = "data"
        const val KEY_SHARE = "key"
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction("PHUC")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
        val intent = Intent(this, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(
            this, REQUEST_CODE, intent, 0
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
        registerNotificationChannel()
        createNotification()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun registerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification() {

        notification.setContentIntent(pendingIntent)
            .setContentTitle("Hello")
            .setContentText("My App Test")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.icon_notify)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        startForeground(NOTIFICATION_ID, notification.build())

    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "PHUC") {
                Log.d("dog", "log")
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                context?.startActivity(intent)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

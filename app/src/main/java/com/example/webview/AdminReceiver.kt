package com.example.webview

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class AdminReceiver : DeviceAdminReceiver() {

        override fun onEnabled(context: Context, intent: Intent) {
            super.onEnabled(context, intent)
            KioskUtils(context).setLockTaskPackage()
        }
    }
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    override fun onEnabled(context: Context, intent: Intent) {
//        super.onEnabled(context, intent)
//        context.run {
//            val deviceAdmin = ComponentName(this, AdminReceiver::class.java)
//            val dpm = this.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//            dpm.setLockTaskPackages(deviceAdmin, arrayOf(this.packageName))
//
//        }
//    }

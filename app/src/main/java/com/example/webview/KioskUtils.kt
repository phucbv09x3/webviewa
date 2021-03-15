package com.example.webview

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi


class KioskUtils(private val context: Context) {
    private val deviceAdmin = ComponentName(context, AdminReceiver::class.java)
    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setLockTaskPackage() =
        dpm.setLockTaskPackages(deviceAdmin, arrayOf(context.packageName))

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun start(activity: Activity) {
        activity.startLockTask()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun clearDeviceOwner() {
        if (dpm.isDeviceOwnerApp(context.packageName)) {
            dpm.clearDeviceOwnerApp(context.packageName)
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun stop(activity: Activity) {
        activity.stopLockTask()

    }

}
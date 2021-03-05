package com.example.webview

import android.app.Activity
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageInstaller.SessionParams
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.*


class KioskUtils(private val context: Context) {
    private val deviceAdmin = ComponentName(context, AdminReceiver::class.java)
    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setHomeActivity(activity: Activity) {
        val intentFilter = IntentFilter(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addCategory(Intent.CATEGORY_HOME)
        }

        val home = ComponentName(context, activity::class.java)
        dpm.addPersistentPreferredActivity(deviceAdmin, intentFilter, home)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun resetHomeActivity() =
        dpm.clearPackagePersistentPreferredActivities(deviceAdmin, context.packageName)

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun hasDeviceOwnerPermission(): Boolean =
        dpm.isAdminActive(deviceAdmin) && dpm.isDeviceOwnerApp(context.packageName)

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun clearDeviceOwner() {
        if (dpm.isDeviceOwnerApp(context.packageName)) {
           // dpm.clearPackagePersistentPreferredActivities(deviceAdmin,context.packageName)
            dpm.clearDeviceOwnerApp(context.packageName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setLockTaskPackage() =
        dpm.setLockTaskPackages(deviceAdmin, arrayOf(context.packageName))

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun start(activity: Activity) {
        activity.startLockTask()

        if (hasDeviceOwnerPermission()) {
            setHomeActivity(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun stop(activity: Activity) {
        activity.stopLockTask()

        if (hasDeviceOwnerPermission()) {
            resetHomeActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun installPackage(file: File) {
        val packageInstaller = context.packageManager.packageInstaller
        val params = SessionParams(SessionParams.MODE_FULL_INSTALL).apply {
            setInstallLocation(PackageInfo.INSTALL_LOCATION_AUTO)
        }

        Log.d("param", "$params")
        val sessionId = packageInstaller.createSession(params)
        Log.d("param1", "$sessionId")
        packageInstaller.openSession(sessionId).use { session ->
            session.openWrite("", 0, file.length()).use { output ->
                FileInputStream(file).use { input ->
                    input.copyTo(output)
                    session.fsync(output)

                }
            }
            val dummySender = PendingIntent.getBroadcast(
                context,
                sessionId, Intent(""), 0
            ).intentSender
            session.commit(dummySender)
        }
    }

}
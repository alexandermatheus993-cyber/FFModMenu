package com.ff.modmenu

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {
    companion object {
        const val CHANNEL_ID = "ff_mod_channel"
    }
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CHANNEL_ID, "FF Mod Menu", NotificationManager.IMPORTANCE_LOW)
            ch.description = "Mod menu"
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
    }
}

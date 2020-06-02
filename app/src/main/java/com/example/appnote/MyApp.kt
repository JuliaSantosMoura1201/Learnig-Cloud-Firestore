package com.example.appnote

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.example.appnote.notify.NotificationHelper

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel")
    }
}
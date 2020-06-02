package com.example.appnote

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.example.appnote.notify.NotificationHelper
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("Notes.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        Realm.setDefaultConfiguration(configuration)

        NotificationHelper.createNotificationChannel(this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel")
    }
}
package com.example.appnote.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.appnote.MainActivity
import com.example.appnote.Notes
import com.example.appnote.R

object NotificationHelper {

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "${context.packageName}--$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createSampleDataNotification(context: Context, notes: Notes, autoCancel: Boolean){
        val channelId = "${context.packageName}--${context.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
            setContentTitle(notes.title)
            setStyle(NotificationCompat.BigTextStyle().bigText(notes.description))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0,intent, 0)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }
}
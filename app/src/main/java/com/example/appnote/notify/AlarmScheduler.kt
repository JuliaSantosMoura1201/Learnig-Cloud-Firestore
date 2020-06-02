package com.example.appnote.notify

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.appnote.Notes
import com.example.appnote.R
import java.util.*

object AlarmScheduler {

    fun scheduleAlarmsForReminder(context: Context, notes: Notes){
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val day = notes.day
        if(day != null){
            val alarmIntent = createPendingIntent(context, notes, day)
            scheduleAlarm(notes, alarmIntent, alarmMgr)
        }
    }

    private fun scheduleAlarm(notes: Notes, alarmIntent: PendingIntent?, alarmMgr: AlarmManager){
        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())

        val hour = notes.hour?.toInt() ?: 20
        val min = notes.minute?.toInt()  ?: 50

        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(Calendar.HOUR_OF_DAY, hour)
        datetimeToAlarm.set(Calendar.MINUTE, min)
        datetimeToAlarm.set(Calendar.SECOND, 0)
        datetimeToAlarm.set(Calendar.MILLISECOND, 0)

        val today = Calendar.getInstance(Locale.getDefault())
        if(shouldNotifyToday(today, datetimeToAlarm)){
            alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP,
                datetimeToAlarm.timeInMillis, (1000 * 60 * 60 * 24 * 7).toLong(), alarmIntent)
            return
        }

        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            datetimeToAlarm.timeInMillis, (1000 * 60 * 60 * 24 * 7).toLong(), alarmIntent)
    }

    private fun createPendingIntent(context: Context, notes: Notes, day: String?): PendingIntent {

        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = context.getString(R.string.action_notify_task)
            type = "$day-${notes.title}--${notes.description}"
            putExtra("id",  notes.id)
        }

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun shouldNotifyToday(today: Calendar, datetimeToAlarm: Calendar): Boolean {
        return today.get(Calendar.HOUR_OF_DAY) <= datetimeToAlarm.get(Calendar.HOUR_OF_DAY) &&
                today.get(Calendar.MINUTE) <= datetimeToAlarm.get(Calendar.MINUTE)
    }
}
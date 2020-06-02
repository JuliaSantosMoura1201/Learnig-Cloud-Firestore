package com.example.appnote.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.appnote.Notes
import com.example.appnote.R
import io.realm.Realm
import io.realm.RealmResults

class AlarmReceiver : BroadcastReceiver(){

    private lateinit var realm: Realm

    override fun onReceive(context: Context?, intent: Intent?) {
        val id: Int
        if (context != null && intent != null && intent.action != null){
            if(intent.action!!.equals(context.getString(R.string.action_notify_task), ignoreCase = true)){
                if(intent.extras != null){
                    id = intent.extras!!.getInt("id")

                    realm = Realm.getDefaultInstance()
                    val tasks: RealmResults<Notes> = realm.where(Notes::class.java).findAll()
                    var notes: Notes? = tasks.where().equalTo("id", id-1).findFirst()

                    if (notes != null){
                        NotificationHelper.createSampleDataNotification(context, notes, false)
                    }else{
                        notes = Notes(0, "deu", "ruim")
                        NotificationHelper.createSampleDataNotification(context, notes, false)
                    }
                }
            }
        }
    }
}
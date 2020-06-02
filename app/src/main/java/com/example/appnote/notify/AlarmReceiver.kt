package com.example.appnote.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.appnote.model.Note
import com.example.appnote.R
import com.google.firebase.firestore.FirebaseFirestore

class AlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        val description: String
        val title: String
        if (context != null && intent != null && intent.action != null) {
            if (intent.action!!.equals(context.getString(R.string.action_notify_task), ignoreCase = true)) {
                if (intent.extras != null) {
                    description = intent.extras!!.getString("description").toString()
                    title = intent.extras!!.getString("title").toString()

                    val firebaseFirestore = FirebaseFirestore.getInstance()
                    firebaseFirestore.collection("notes")
                        .whereEqualTo("title", title)
                        .whereEqualTo("description", description)
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            if (firebaseFirestoreException != null) {
                                return@addSnapshotListener
                            }
                            if (querySnapshot != null) {
                                val note = Note()
                                for (doc in querySnapshot) {
                                    doc.getString("title")?.let {
                                        note.title = it
                                    }
                                    doc.getString("description")?.let {
                                        note.description = it
                                    }
                                }

                                NotificationHelper.createSampleDataNotification(
                                    context,
                                    note,
                                    false
                                )
                            } else {
                                val note = Note(
                                    "",
                                    "deu",
                                    "ruim"
                                )
                                NotificationHelper.createSampleDataNotification(
                                    context,
                                    note,
                                    false
                                )
                            }
                        }
                }

            }
        }
    }
}
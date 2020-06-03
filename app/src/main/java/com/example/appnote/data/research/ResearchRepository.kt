package com.example.appnote.data.research

import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResearchRepository {

    private var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    fun makeResearch(type: String, item: String): MutableLiveData<Note> {

        val liveDataResponse: MutableLiveData<Note> = MutableLiveData()

        firebaseFirestore.collection("notes")
            .whereEqualTo("userEmail", user.currentUser?.email)
            .whereEqualTo(type, item)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    return@addSnapshotListener
                }
                if (querySnapshot == null || querySnapshot.isEmpty){
                    return@addSnapshotListener
                }

                for (doc in querySnapshot){
                    val note = Note()
                    doc.getString("title")?.let {
                      note.title = it
                    }
                    doc.getString("description")?.let {
                       note.description = it
                    }
                    doc.getBoolean("state")?.let {
                        note.state = it
                    }
                    note.id = doc.id
                    liveDataResponse.postValue(note)
                }
            }

        return liveDataResponse
    }
}
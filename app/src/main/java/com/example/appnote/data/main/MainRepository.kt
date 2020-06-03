package com.example.appnote.data.main

import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainRepository {

    private var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference: StorageReference = firebaseStorage.reference
    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    fun getNotes(): MutableLiveData<ArrayList<Note>>{

        val liveDataResponse: MutableLiveData<ArrayList<Note>> = MutableLiveData()
        val list = ArrayList<Note>()

        firebaseFirestore.collection("notes")
            .whereEqualTo("userEmail", user.currentUser?.email)
            .get()
            .addOnSuccessListener{ documents ->
                for (doc in documents!!){
                    val note = Note()
                    note.id = doc.id
                    doc.getString("title")?.let {
                        note.title = it
                    }
                    doc.getString("description")?.let {
                        note.description = it
                    }
                    doc.getBoolean("state")?.let {
                        note.state = it
                    }
                    list.add(note)
                }
                liveDataResponse.postValue(list)
            }

        return liveDataResponse
    }

    fun updateState(id: String): MutableLiveData<Boolean> {

        val liveDataResponse = MutableLiveData<Boolean>()

        firebaseFirestore.collection("notes")
            .document(id)
            .get()
            .addOnSuccessListener { doc ->
                val note = Note()

                if (doc != null) {

                    doc.getString("title")?.let {
                        note.title = it
                    }
                    doc.getString("description")?.let {
                        note.description = it
                    }
                    doc.getString("place")?.let {
                        note.place = it
                    }

                    doc.getString("year")?.let {
                        note.year = it
                    }

                    doc.getString("month")?.let {
                        note.month = it
                    }

                    doc.getString("day")?.let {
                        note.day = it
                    }

                    doc.getString("hour")?.let {
                        note.hour = it
                    }

                    doc.getString("minute")?.let{
                        note.minute = it
                    }

                    doc.getBoolean("state")?.let {
                        note.state = !it
                    }

                    note.userEmail = user.currentUser?.email
                    firebaseFirestore.collection("notes")
                        .document(id)
                        .set(note)

                    liveDataResponse.postValue(true)
                }
            }
            .addOnFailureListener { liveDataResponse.postValue(false) }

        return liveDataResponse
    }

    fun deleteNote(id: String): MutableLiveData<Boolean>{

        val liveDataResponse = MutableLiveData<Boolean>()
        firebaseFirestore.collection("notes")
            .document(id)
            .delete()
            .addOnFailureListener {liveDataResponse.postValue(false)}
            .addOnSuccessListener {liveDataResponse.postValue(true) }
        return liveDataResponse
    }

    fun deleteImage(id: String): MutableLiveData<Boolean>{
        val liveDataResponse = MutableLiveData<Boolean>()

        firebaseFirestore.collection("notes")
            .document(id)
            .get()
            .addOnSuccessListener {doc ->
                val note = Note()
                doc.getString("title")?.let {
                    note.title = it
                }
                doc.getString("description")?.let {
                    note.description = it
                }
                doc.getString("year")?.let {
                    note.year = it
                }

                doc.getString("month")?.let {
                    note.month = it
                }

                doc.getString("day")?.let {
                    note.day = it
                }

                val name = note.title.plus(note.description).plus(note.day).plus(note.month).plus(note.year)
                val imageRef = storageReference.child(name)
                imageRef.delete()
                    .addOnSuccessListener {
                        liveDataResponse.postValue(true)
                    }
                    .addOnFailureListener{
                        liveDataResponse.postValue(false)
                    }
            }

        return liveDataResponse
    }
    fun deleteAll(): MutableLiveData<Boolean>{

        val liveDataResponse = MutableLiveData<Boolean>()

        firebaseFirestore.collection("notes")
            .get()
            .addOnSuccessListener{ documents ->
                val deleteList = java.util.ArrayList<String>()
                for (doc in documents!!){
                    deleteList.add(doc.id)
                }

                for (doc in deleteList){
                    firebaseFirestore.collection("notes")
                        .document(doc)
                        .delete()
                }

                liveDataResponse.postValue(true)
            }
            .addOnFailureListener { liveDataResponse.postValue(false) }
        return liveDataResponse
    }

    fun deleteAllImages(): MutableLiveData<Boolean>{

        val liveDataResponse = MutableLiveData<Boolean>()

        firebaseFirestore.collection("notes")
            .get()
            .addOnSuccessListener{ documents ->
                for (doc in documents!!){
                    firebaseFirestore.collection("notes")
                        .document(doc.id)
                        .get()
                        .addOnSuccessListener {document ->
                            val note = Note()
                            document.getString("title")?.let {
                                note.title = it
                            }
                            document.getString("description")?.let {
                                note.description = it
                            }
                            document.getString("year")?.let {
                                note.year = it
                            }

                            document.getString("month")?.let {
                                note.month = it
                            }

                            document.getString("day")?.let {
                                note.day = it
                            }

                            val name = note.title.plus(note.description).plus(note.day).plus(note.month).plus(note.year)
                            val imageRef = storageReference.child(name)
                            imageRef.delete()
                                .addOnSuccessListener {
                                    liveDataResponse.postValue(true)
                                }
                                .addOnFailureListener{
                                    liveDataResponse.postValue(false)
                                }
                        }
                }
            }
            .addOnFailureListener { liveDataResponse.postValue(false) }
        return liveDataResponse
    }

    fun logOut(){
        user.signOut()
    }
}


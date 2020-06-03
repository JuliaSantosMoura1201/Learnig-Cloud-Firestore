package com.example.appnote.data.add

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SaveNoteRepository {

    private var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference: StorageReference = firebaseStorage.reference


    fun addNotes(note: Note, filePath: Uri): MutableLiveData<Boolean> {

        val liveDataResponse = MutableLiveData<Boolean>()

        firebaseFirestore.collection("notes")
            .add(note)
            .addOnSuccessListener {
                val name = note.title.plus(note.description).plus(note.day).plus(note.month).plus(note.year)
                val imageRef = storageReference.child(name)
                imageRef.putFile(filePath)
                    .addOnSuccessListener {
                        liveDataResponse.postValue(true)
                    }
                    .addOnFailureListener{
                        liveDataResponse.postValue(false)
                    }
            }
            .addOnFailureListener {
                liveDataResponse.postValue(false)
            }

        return liveDataResponse
    }

    fun replaceNotes(id: String, note: Note, filePath: Uri?): MutableLiveData<Boolean> {

        val liveDataResponse = MutableLiveData<Boolean>()
        firebaseFirestore.collection("notes")
            .document(id)
            .set(note)
            .addOnSuccessListener {
                val name = note.title.plus(note.description).plus(note.day).plus(note.month).plus(note.year)
                val imageRef = storageReference.child(name)
                if(filePath != null){
                    imageRef.putFile(filePath)
                        .addOnSuccessListener {
                            liveDataResponse.postValue(true)
                        }
                        .addOnFailureListener{
                            liveDataResponse.postValue(false)
                        }
                }

            }
            .addOnFailureListener {
                liveDataResponse.postValue(false)
            }

        return liveDataResponse
    }

    fun getNote(id: String): MutableLiveData<Note> {

        val liveDataResponse : MutableLiveData<Note> = MutableLiveData()

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

                    liveDataResponse.postValue(note)
                }
            }
        return liveDataResponse
    }

    fun getImage(name: String): MutableLiveData<ByteArray>{

        val liveDataResponse : MutableLiveData<ByteArray> = MutableLiveData()
        val imageRef = storageReference.child(name)
        val ONE_MEGABYTE: Long = 1024 * 1024

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            liveDataResponse.postValue(it)
        }

        return liveDataResponse
    }
}
package com.example.appnote.data.main

import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.Note
import com.example.appnote.model.NoteRealmDb
import com.example.appnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.realm.Realm
import io.realm.RealmResults

class MainRepository {

    private lateinit var realm: Realm
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

        val imageRef = storageReference.child(id)
        imageRef.delete()
            .addOnSuccessListener {
                liveDataResponse.postValue(true)
            }
            .addOnFailureListener{
                liveDataResponse.postValue(false)
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
                        .addOnSuccessListener {
                            val imageRef = storageReference.child(doc.id)
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

    fun getUser(): MutableLiveData<User>{
        val liveDataResponse : MutableLiveData<User> = MutableLiveData()

        firebaseFirestore.collection("user")
            .whereEqualTo("email", user.currentUser?.email)
            .addSnapshotListener { querySnapshot, _ ->

                if(querySnapshot != null){
                    for (doc in querySnapshot){
                        val user = User("", "")

                        doc.getString("name")?.let {
                            user.name = it
                        }

                        doc.getString("email")?.let {
                            user.email = it
                        }

                        liveDataResponse.postValue(user)
                    }
                }

            }

        return  liveDataResponse
    }

    fun getImage(): MutableLiveData<ByteArray>{

        val liveDataResponse : MutableLiveData<ByteArray> = MutableLiveData()
        val id = user.currentUser?.email
        val imageRef = storageReference.child(id!!)
        val ONE_MEGABYTE: Long = 1024 * 1024

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            liveDataResponse.postValue(it)
        }

        return liveDataResponse
    }

    fun logOut(){
        user.signOut()
    }

    fun receiveListOfNotesFromFirebaseAndInsertOnRealmDb(listOfNotes: ArrayList<Note>): RealmResults<NoteRealmDb> {
        realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()

        for (note in listOfNotes){
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(setDataToNote(autoIncrementId(), note))
            realm.commitTransaction()
        }

        return realm.where(NoteRealmDb::class.java).findAll()
    }

    private fun autoIncrementId(): Int{
        val currentIdNumber: Number? = realm.where(NoteRealmDb::class.java).max("id")
        val nextID: Int

        nextID = if (currentIdNumber == null){
            1
        }else{
            currentIdNumber.toInt() + 1
        }
        return nextID
    }


    private fun setDataToNote(nextID: Int, note: Note): NoteRealmDb{
        return NoteRealmDb(
            nextID,
            note.title,
            note.description,
            note.day,
            note.month,
            note.year,
            note.hour,
            note.minute,
            note.place,
            note.state,
            note.userEmail,
            note.notificationOn,
            note.alarmOn,
            note.id
        )

    }
}


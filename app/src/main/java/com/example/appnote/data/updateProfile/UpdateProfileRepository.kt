package com.example.appnote.data.updateProfile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateProfileRepository {

    private var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference: StorageReference = firebaseStorage.reference
    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    fun getUser(): MutableLiveData<User> {
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

    fun getImage(): MutableLiveData<ByteArray> {

        val liveDataResponse : MutableLiveData<ByteArray> = MutableLiveData()
        val id = user.currentUser?.email
        val imageRef = storageReference.child(id!!)
        val ONE_MEGABYTE: Long = 1024 * 1024

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            liveDataResponse.postValue(it)
        }

        return liveDataResponse
    }

    fun updateUser(name: String): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>(false)

        val updateUser = user.currentUser?.email?.let { User( name, it) }
        firebaseFirestore.collection("user")
            .whereEqualTo("email", user.currentUser?.email)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(querySnapshot != null){

                    for (doc in querySnapshot){

                        if (updateUser != null) {
                            firebaseFirestore.collection("user").document(doc.id)
                                .set(updateUser)
                                .addOnSuccessListener { liveData.postValue(true) }
                        }
                    }
                }
            }
        return liveData
    }

    fun updateImage(filePath: Uri): MutableLiveData<Boolean>{
        val liveDataResponse = MutableLiveData<Boolean>(false)
        val id = user.currentUser?.email
        storageReference.child(id!!).putFile(filePath)
            .addOnSuccessListener {
                liveDataResponse.postValue(true)
            }
        return liveDataResponse
    }

    fun updatePassword(): MutableLiveData<Boolean>{
        val liveData = MutableLiveData<Boolean>(false)
        user.sendPasswordResetEmail(user.currentUser?.email!!)
            .addOnSuccessListener {
                liveData.postValue(true)
            }
        return liveData
    }

    fun logOut(){
        user.signOut()
    }
}
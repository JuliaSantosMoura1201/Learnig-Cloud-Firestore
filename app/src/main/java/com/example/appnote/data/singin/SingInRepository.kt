package com.example.appnote.data.singin

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.appnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SingInRepository {

    private var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference: StorageReference = firebaseStorage.reference
    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    fun singIn(email: String, password: String): MutableLiveData<Boolean> {
        val liveDataResponse =  MutableLiveData<Boolean>()
        user.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    user.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener{
                            if(task.isSuccessful){
                                liveDataResponse.postValue(true)
                            }else{
                                liveDataResponse.postValue(false)
                            }
                        }
                }else{
                    liveDataResponse.postValue(false)
                }
            }

        return liveDataResponse
    }

    fun saveUser(user: User): MutableLiveData<String>{

        val liveDataResponse = MutableLiveData<String>()

        firebaseFirestore.collection("user")
            .add(user)
            .addOnSuccessListener {
                liveDataResponse.postValue(user.email)
            }
            .addOnFailureListener {
                liveDataResponse.postValue("")
            }

        return liveDataResponse
    }

    fun saveProfilePhoto(id: String, filePath: Uri): MutableLiveData<Boolean>{
        val liveDataResponse = MutableLiveData<Boolean>()
        val imageRef = storageReference.child(id)
        imageRef.putFile(filePath)
            .addOnSuccessListener {
                liveDataResponse.postValue(true)
            }
            .addOnFailureListener{
                liveDataResponse.postValue(false)
            }

        return liveDataResponse
    }
}
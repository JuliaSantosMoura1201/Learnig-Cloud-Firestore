package com.example.appnote.data.login

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LoginRepository {

    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    fun logIn(email: String, password: String): MutableLiveData<Boolean> {

        val liveDataResponse = MutableLiveData<Boolean>()

        if(user.currentUser != null){
            liveDataResponse.postValue(true)
            return liveDataResponse
        }else{
            user.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        liveDataResponse.postValue(true)
                    }else{
                        liveDataResponse.postValue(false)
                    }
                }
        }


        return liveDataResponse
    }
}
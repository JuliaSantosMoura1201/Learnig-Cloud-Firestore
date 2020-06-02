package com.example.appnote.data.singin

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class SingInRepository {

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
}
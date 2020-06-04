package com.example.appnote.data.splash

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class SplashScreenRepository {


    private val user: FirebaseAuth = FirebaseAuth.getInstance()
    fun isLogged(): MutableLiveData<Boolean> {

        val liveDataResponse = MutableLiveData<Boolean>(false)

        if(user.currentUser != null){
            liveDataResponse.postValue(true)
        }

        return liveDataResponse
    }
}
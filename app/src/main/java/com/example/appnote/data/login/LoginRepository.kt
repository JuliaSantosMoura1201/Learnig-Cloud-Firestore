package com.example.appnote.data.login

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginRepository {

    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    fun logIn(email: String, password: String): MutableLiveData<Boolean> {

        val liveDataResponse = MutableLiveData<Boolean>()

        user.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task->
                if(task.isSuccessful){
                    liveDataResponse.postValue(true)
                }else{
                    liveDataResponse.postValue(false)
                }
            }

        return liveDataResponse
    }
}
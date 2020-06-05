package com.example.appnote.view.login

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.login.LoginRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class LoginViewModel: ViewModel() {

    val repository: LoginRepository = LoginRepository()

    fun login(email: String ,password: String): LiveData<Boolean>{
       return repository.logIn(email, password)
    }

    fun isPasswordValid(password: String): Boolean{
        if(password.isEmpty()){
            return false
        }
        return true
    }

    fun isEmailValid(email: String): Boolean{
        if(email.isEmpty()){
            return false
        }
        return true
    }
}
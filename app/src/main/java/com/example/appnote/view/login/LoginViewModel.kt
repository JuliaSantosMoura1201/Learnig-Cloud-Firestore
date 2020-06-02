package com.example.appnote.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.login.LoginRepository

class LoginViewModel: ViewModel() {

    val repository: LoginRepository = LoginRepository()

    fun login(email: String ,password: String): LiveData<Boolean>{
       return repository.logIn(email, password)
    }
}
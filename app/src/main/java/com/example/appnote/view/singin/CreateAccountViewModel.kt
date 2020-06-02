package com.example.appnote.view.singin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.singin.SingInRepository

class CreateAccountViewModel: ViewModel() {

    val repository: SingInRepository = SingInRepository()

    fun singInState(email: String, password: String): LiveData<Boolean>{
        return repository.singIn(email, password)
    }
}
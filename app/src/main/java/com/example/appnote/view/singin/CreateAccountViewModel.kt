package com.example.appnote.view.singin

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.singin.SingInRepository
import com.example.appnote.model.User

class CreateAccountViewModel: ViewModel() {

    val repository: SingInRepository = SingInRepository()

    fun singInState(email: String, password: String): LiveData<Boolean>{
        return repository.singIn(email, password)
    }

    fun isPasswordValid(password: String): Boolean{
        if(password.isNotEmpty()){
            if (password.length >= 6){
                return true
            }
        }
        return false
    }

    fun isEmailValid(email: String): Boolean{
        if(email.isEmpty()){
            return false
        }
        return true
    }

    fun saveUser(user: User): LiveData<String>{
        return repository.saveUser(user)
    }

    fun saveProfilePhoto(id: String, filePath: Uri): LiveData<Boolean>{
        return repository.saveProfilePhoto(id, filePath)
    }
}
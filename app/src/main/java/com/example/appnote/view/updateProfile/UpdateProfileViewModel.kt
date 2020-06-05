package com.example.appnote.view.updateProfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.updateProfile.UpdateProfileRepository
import com.example.appnote.model.User

class UpdateProfileViewModel : ViewModel(){

    val repository: UpdateProfileRepository = UpdateProfileRepository()

    fun getUser(): LiveData<User>{
        return repository.getUser()
    }

    fun getImage(): LiveData<ByteArray>{
        return repository.getImage()
    }

    fun updateUser(name: String): LiveData<Boolean>{
        return repository.updateUser(name)
    }

    fun updateProfilePic(filePath: Uri): LiveData<Boolean>{
        return repository.updateImage(filePath)
    }

    fun updatePassword(): LiveData<Boolean>{
        return repository.updatePassword()
    }

    fun logOut(){
        repository.logOut()
    }
}
package com.example.appnote.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.main.MainRepository
import com.example.appnote.model.Note

class MainViewModel: ViewModel() {

    val repository: MainRepository = MainRepository()

    fun getNotes(): LiveData<ArrayList<Note>>{
        return repository.getNotes()
    }

    fun updateState(id: String): LiveData<Boolean>{
        return repository.updateState(id)
    }

    fun deleteNote(id: String): LiveData<Boolean>{
        return repository.deleteNote(id)
    }

    fun deleteImage(id: String): LiveData<Boolean>{
        return repository.deleteImage(id)
    }
    fun deleteAll(): LiveData<Boolean>{
        return repository.deleteAll()
    }

    fun deleteAllImages(): LiveData<Boolean>{
        return  repository.deleteAllImages()
    }

    fun logOut(){
        repository.logOut()
    }
}
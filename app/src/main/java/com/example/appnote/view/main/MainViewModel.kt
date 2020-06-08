package com.example.appnote.view.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.main.MainRepository
import com.example.appnote.model.Note
import com.example.appnote.model.NoteRealmDb
import com.example.appnote.model.User
import io.realm.RealmResults

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

    fun getUser(): LiveData<User>{
        return repository.getUser()
    }

    fun getImage(): LiveData<ByteArray>{
        return  repository.getImage()
    }
    fun logOut(){
        repository.logOut()
    }

    fun hasInternetConnected(context: Context): Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun receiveListOfNotesFromFirebaseAndInsertOnRealmDb(listOfNotes: ArrayList<Note>): RealmResults<NoteRealmDb> {
        return repository.receiveListOfNotesFromFirebaseAndInsertOnRealmDb(listOfNotes)
    }
}
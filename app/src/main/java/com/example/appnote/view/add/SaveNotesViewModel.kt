package com.example.appnote.view.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.add.SaveNoteRepository
import com.example.appnote.model.Note

class SaveNotesViewModel: ViewModel() {

    private val repository: SaveNoteRepository = SaveNoteRepository()

    fun addNote(note: Note, filePath: Uri): LiveData<Boolean>{
       return  repository.addNotes(note, filePath)
    }

    fun replaceNote(id: String, note: Note): LiveData<Boolean> {
        return repository.replaceNotes(id, note)
    }

    fun getNote(id: String): LiveData<Note>{
        return  repository.getNote(id)
    }
}
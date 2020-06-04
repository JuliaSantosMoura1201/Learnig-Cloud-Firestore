package com.example.appnote.view.save

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.save.SaveNoteRepository
import com.example.appnote.model.Note
import java.util.*

class SaveNotesViewModel: ViewModel() {

    private val repository: SaveNoteRepository = SaveNoteRepository()

    fun addNote(note: Note): LiveData<String> {
        return repository.addNotes(note)
    }

    fun addImage(id: String, filePath: Uri): LiveData<Boolean>{
        return repository.addImage(id, filePath)
    }

    fun replaceNote(id: String, note: Note, filePath: Uri?): LiveData<Boolean> {
        return repository.replaceNotes(id, note, filePath)
    }

    fun getNote(id: String): LiveData<Note> {
        return repository.getNote(id)
    }

    fun getImage(name: String): LiveData<ByteArray> {
        return repository.getImage(name)
    }

    fun isFieldEmpty(field: String): Boolean {
        if(field.isEmpty()){
            return true
        }
        return false
    }

    fun validateDate(date: String): Boolean {
        val intDay = date.substring(0, 2).toInt()
        val intMonth = date.substring(3, 5).toInt()
        val intYear = date.substring(6, 10).toInt()

        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val currentMonth = c.get(Calendar.MONTH)
        val currentDay = c.get(Calendar.DAY_OF_MONTH)

        if (intYear >= currentYear) {
            if (intMonth >= currentMonth) {
                if (intDay >= currentDay) {
                    return true
                }
                return false
            }
            return false
        }
        return false
    }

}
package com.example.appnote.view.research

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.research.ResearchRepository
import com.example.appnote.model.Note

class ResearchViewModel: ViewModel() {

    private val repository: ResearchRepository = ResearchRepository()

    fun research(type: String, item: String): LiveData<Note>{
        return repository.makeResearch(type, item)
    }

    fun isResearchValid(item: String): Boolean {
        if(item.isEmpty()){
            return false
        }
        return true
    }
}
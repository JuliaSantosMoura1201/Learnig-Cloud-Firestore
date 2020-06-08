package com.example.appnote

import com.example.appnote.view.save.SaveNotesViewModel
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SaveNotesTest {

    private var viewModelTest: SaveNotesViewModel = SaveNotesViewModel()

    @Test
    fun `is date invalid when year is in the past`(){
        val answer = viewModelTest.validateDate("08/06/2019")
        assertEquals(false, answer)
    }
}
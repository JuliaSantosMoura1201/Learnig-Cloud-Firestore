package com.example.appnote.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appnote.view.login.LoginActivity
import com.example.appnote.R
import com.example.appnote.view.research.ResearchActivity
import com.example.appnote.model.Note
import com.example.appnote.view.add.AddNotesActivity
import com.example.appnote.view.delete.DeleteDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var addNotes: FloatingActionButton
    private lateinit var notesRV: RecyclerView
    private var notesList = ArrayList<Note>()
    var idDialog = ""
    val deleteDialog= DeleteDialog.newInstance(this)

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNotes = findViewById(R.id.addNotes)
        notesRV = findViewById(R.id.notesRV)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        getAllNotes()

        addNotes.setOnClickListener {
            startActivity(Intent(this, AddNotesActivity::class.java))
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        notesRV.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun getAllNotes(){
        notesList.clear()
        viewModel.getNotes().observe(this, Observer {
            fillAdapter(it)
        })

    }

    private fun fillAdapter(list: ArrayList<Note>){
        notesRV.adapter = NotesAdapter(list, getScreenWidth(),
            object : NotesAdapter.NotesListener {
                override fun deleteNote(id: String) {
                    supportFragmentManager.beginTransaction().add(deleteDialog, null).commitAllowingStateLoss()
                    idDialog = id
                }

                override fun editNote(id: String) {
                    idDialog = id
                    goToAddActivity()
                }

                override fun changeState(id: String) {
                    idDialog = id
                    fieldComponents()
                }

            })

        notesRV.adapter!!.notifyDataSetChanged()
    }

    private fun fieldComponents(){
        viewModel.updateState(idDialog).observe(this, Observer {
            if (it){
                getAllNotes()
            }
        })
    }

    private fun getScreenWidth(): Int{
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteOption -> {
               deleteAll()
            }

            R.id.researchOption -> {
                startActivity(Intent(this, ResearchActivity::class.java))
            }
            else -> {
                viewModel.logOut()
                goToLogin()
            }
        }

        return true
    }

    fun deleteRealm(){
        viewModel.deleteNote(idDialog).observe(this, Observer {
            if(it){
                getAllNotes()
            }
        })
    }

    private fun deleteAll(){
        viewModel.deleteAll().observe(this, Observer {
            if (it){
                getAllNotes()
            }
        })
    }

    private fun goToLogin(){
        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToAddActivity(){
        val intent = Intent(this@MainActivity, AddNotesActivity::class.java)
        intent.putExtra("id", idDialog)
        startActivity(intent)
    }
}

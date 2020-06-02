package com.example.appnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var addNotes: FloatingActionButton
    private lateinit var notesRV: RecyclerView
    var idDialog = ""
    val deleteDialog= DeleteDialog.newInstance(this)

    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNotes = findViewById(R.id.addNotes)
        notesRV = findViewById(R.id.notesRV)


        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("notes")
            .get()
            .addOnSuccessListener{ documents ->
                val notesList = ArrayList<Note>()
                for (doc in documents!!){
                    val note = Note()
                    note.id = doc.id
                    doc.getString("title")?.let {
                        note.title = it
                    }
                    doc.getString("description")?.let {
                       note.description = it
                    }
                    notesList.add(note)
                }
                getAllNotes(notesList)
            }

        addNotes.setOnClickListener {
            startActivity(Intent(this, AddNotesActivity::class.java))
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        notesRV.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        //getAllNotes()
    }

    private fun getAllNotes(list: ArrayList<Note>){

        notesRV.adapter = NotesAdapter(list, getScreenWidth(), object : NotesAdapter.NotesListener {
            override fun deleteNote(id: String) {
                supportFragmentManager.beginTransaction().add(deleteDialog, null).commitAllowingStateLoss()
                idDialog = id
            }

            override fun editNote(id:String) {
                idDialog = id
                val intent = Intent(this@MainActivity, AddNotesActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }

        })

        notesRV.adapter!!.notifyDataSetChanged()
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
                firebaseFirestore.collection("notes")
                    .get()
                    .addOnSuccessListener{ documents ->
                        val deleteList = ArrayList<String>()
                        for (doc in documents!!){
                            deleteList.add(doc.id)
                        }

                        for (item in deleteList){
                            firebaseFirestore.collection("notes")
                                .document(item)
                                .delete()
                        }
                    }

                //getAllNotes()
            }
            R.id.researchOption -> {
                startActivity(Intent(this, ResearchActivity::class.java))
            }
            else -> {
                val user: FirebaseAuth = FirebaseAuth.getInstance()
                user.signOut()
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return true
    }

    fun deleteRealm(){
        firebaseFirestore.collection("notes")
            .document(idDialog)
            .delete()
        //getAllNotes()
    }
}

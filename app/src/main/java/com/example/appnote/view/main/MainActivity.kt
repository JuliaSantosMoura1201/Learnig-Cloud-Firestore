package com.example.appnote.view.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.crashlytics.android.Crashlytics
import com.example.appnote.R
import com.example.appnote.model.Note
import com.example.appnote.view.customize.CustomizeActivity
import com.example.appnote.view.save.SaveNotesActivity
import com.example.appnote.view.delete.DeleteDialog
import com.example.appnote.view.login.LoginActivity
import com.example.appnote.view.pockemon.PokemonActivity
import com.example.appnote.view.research.ResearchActivity
import com.google.android.material.navigation.NavigationView
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var notesList = ArrayList<Note>()
    var idDialog = ""
    val deleteDialog= DeleteDialog.newInstance(this)

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configSharedPreferences()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        getAllNotes()

        addNotes.setOnClickListener {
            startActivity(Intent(this, SaveNotesActivity::class.java))
            finish()
        }

        Fabric.with(this, Crashlytics())

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
                restartActivity()
            }else{
                showToast()
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
               deleteAllImages()
            }
        }

        return true
    }

    private fun deleteRealm(){
        viewModel.deleteNote(idDialog).observe(this, Observer {
            if(it){
                restartActivity()
            }else{
                showToast()
            }
        })
    }

    fun deleteImage(){
        viewModel.deleteImage(idDialog).observe(this, Observer {
            if(it){
                deleteRealm()
            }else{
                showToast()
            }
        })
    }
    private fun deleteAll(){
        viewModel.deleteAll().observe(this, Observer {
            if (it){
                getAllNotes()
            }else{
                showToast()
            }
        })
    }

    private fun deleteAllImages(){
        viewModel.deleteAllImages().observe(this, Observer {
            if (it){
                deleteAll()
            }else{
                showToast()
            }
        })
    }

    private fun goToLogin(){
        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToAddActivity(){
        val intent = Intent(this@MainActivity, SaveNotesActivity::class.java)
        intent.putExtra("id", idDialog)
        startActivity(intent)
    }

    private fun showToast(){
        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_research -> {
                startActivity(Intent(this, ResearchActivity::class.java))
            }
            R.id.nav_tips -> {
                startActivity(Intent(this, PokemonActivity::class.java))
            }
            R.id.nav_logout -> {
                viewModel.logOut()
                goToLogin()
            }
            R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_customize -> {
                startActivity(Intent(this, CustomizeActivity::class.java))
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun configSharedPreferences(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        val themeKey = "currentTheme"

        when(sharedPreferences.getString(themeKey, "pattern")){
            "optionYellow" -> theme.applyStyle(R.style.OverlayThemeYellowMain, true)
            "optionPurple" -> theme.applyStyle(R.style.OverlayThemePurpleMain, true)
            "pattern" -> theme.applyStyle(R.style.OverlayThemeLineMain, true)
        }
    }

    private fun  restartActivity(){
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }
}

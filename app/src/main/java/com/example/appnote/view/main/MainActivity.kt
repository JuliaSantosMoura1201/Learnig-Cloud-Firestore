package com.example.appnote.view.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
import com.example.appnote.model.NoteRealmDb
import com.example.appnote.view.customize.CustomizeActivity
import com.example.appnote.view.save.SaveNotesActivity
import com.example.appnote.view.delete.DeleteDialog
import com.example.appnote.view.login.LoginActivity
import com.example.appnote.view.pockemon.PokemonActivity
import com.example.appnote.view.research.ResearchActivity
import com.example.appnote.view.updateProfile.UpdateProfileActivity
import com.google.android.material.navigation.NavigationView
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var notesList = ArrayList<Note>()
    var idDialog = ""
    private val deleteDialog = DeleteDialog.newInstance(this)
    private lateinit var v : View
    private lateinit var viewModel: MainViewModel
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configSharedPreferences()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        realm = Realm.getDefaultInstance()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        v = nav_view.getHeaderView(0)

        getAllNotes()
        getUser()
        getImage()

        addNotes.setOnClickListener {
            goToAddActivity(false)
        }

        Fabric.with(this, Crashlytics())
        notesRV.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun getAllNotes(){
        notesList.clear()
        viewModel.getNotes().observe(this, Observer {listOfNotes ->

            realm.beginTransaction()
            realm.deleteAll()
            realm.commitTransaction()

            for (note in listOfNotes){
                realm.beginTransaction()
                realm.copyToRealmOrUpdate(setDataToNote(autoIncrementId(), note))
                realm.commitTransaction()
            }

            val results: RealmResults<NoteRealmDb> = realm.where<NoteRealmDb>(NoteRealmDb::class.java).findAll()
            fillAdapter(results)

        })


    }

    private fun autoIncrementId(): Int{
        val currentIdNumber: Number? = realm.where(NoteRealmDb::class.java).max("id")
        val nextID: Int

        nextID = if (currentIdNumber == null){
            1
        }else{
            currentIdNumber.toInt() + 1
        }
        return nextID
    }

    private fun setDataToNote(nextID: Int, note: Note): NoteRealmDb{
       return NoteRealmDb(
            nextID,
            note.title,
            note.description,
            note.day,
            note.month,
            note.year,
            note.hour,
            note.minute,
            note.place,
            note.state,
            note.userEmail,
            note.notificationOn,
            note.alarmOn,
            note.id
        )

    }

    private fun fillAdapter(list: RealmResults<NoteRealmDb>){
        notesRV.adapter = NotesAdapter(list, getScreenWidth(),
            object : NotesAdapter.NotesListener {
                override fun deleteNote(id: String) {
                    idDialog = id
                    showDeleteDialog()
                }

                override fun editNote(id: String) {
                    idDialog = id
                    goToAddActivity(true)
                }

                override fun changeState(id: String) {
                    idDialog = id
                    fieldComponents()
                }

            })

        notesRV.adapter!!.notifyDataSetChanged()
    }

    private fun showDeleteDialog(){
        if(viewModel.hasInternetConnected(applicationContext)){
            supportFragmentManager.beginTransaction().add(deleteDialog, null).commitAllowingStateLoss()
        }else{
            showToast(R.string.is_not_connected)
        }
    }

    private fun fieldComponents(){
        viewModel.updateState(idDialog).observe(this, Observer {
            if (it){
                restartActivity()
            }else{
                showToast(R.string.general_error)
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

    private fun deleteNote(){
        viewModel.deleteNote(idDialog).observe(this, Observer {
            if(it){
                restartActivity()
            }else{
                showToast(R.string.general_error)
            }
        })
    }

    fun deleteImage(){
        viewModel.deleteImage(idDialog).observe(this, Observer {
            if(it){
                deleteNote()
            }else{
                showToast(R.string.general_error)
            }
        })
    }

    private fun deleteAll(){
        viewModel.deleteAll().observe(this, Observer {
            if (it){
                getAllNotes()
            }else{
                showToast(R.string.general_error)
            }
        })
    }

    private fun deleteAllImages(){
        if(viewModel.hasInternetConnected(this)){
            viewModel.deleteAllImages().observe(this, Observer {
                if (it){
                    deleteAll()
                }else{
                    showToast(R.string.general_error)
                }
            })
        }else{
           showToast(R.string.is_not_connected)
        }

    }

    private fun showToast(message: Int){
        Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_research -> {
                goToResearchActivity()
            }
            R.id.nav_tips -> {
                goToPokemonActivity()
            }
            R.id.nav_logout -> {
               goToLogin()
            }
            R.id.nav_update -> {
                goToUpdateProfileActivity()
            }
            R.id.nav_customize -> {
                goToCustomizeActivity()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun goToLogin(){
        if(viewModel.hasInternetConnected(this)){
            viewModel.logOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            showToast(R.string.is_not_connected)
        }

    }

    private fun goToAddActivity(putExtra: Boolean){
        if(viewModel.hasInternetConnected(this)){
            val intent = Intent(this@MainActivity, SaveNotesActivity::class.java)
            if (putExtra){
                intent.putExtra("id", idDialog)
            }
            startActivity(intent)
        }else{
            showToast(R.string.is_not_connected)
        }
    }

    private fun goToCustomizeActivity(){
        startActivity(Intent(this, CustomizeActivity::class.java))
        finish()
    }

    private fun goToUpdateProfileActivity(){
        if (viewModel.hasInternetConnected(this)){
            startActivity(Intent(this, UpdateProfileActivity::class.java))
        }else{
            showToast(R.string.is_not_connected)
        }
    }

    private fun goToResearchActivity(){
        if(viewModel.hasInternetConnected(this)){
            startActivity(Intent(this, ResearchActivity::class.java))
        }else{
            showToast(R.string.is_not_connected)
        }
    }

    private fun goToPokemonActivity(){
        if(viewModel.hasInternetConnected(this)){
            startActivity(Intent(this, PokemonActivity::class.java))
        }else{
            showToast(R.string.is_not_connected)
        }
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

    private fun getUser(){
        val name = v.findViewById<TextView>(R.id.txtName)
        val email = v.findViewById<TextView>(R.id.txtEmail)

        viewModel.getUser().observe(this, Observer {
            name.text = it.name
            email.text = it.email
        })
    }

    private fun getImage(){
        val img = v.findViewById<ImageView>(R.id.ivPersonIcon)

        viewModel.getImage().observe(this, Observer {
            if(it != null){
                val bpm = BitmapFactory.decodeByteArray(it, 0, it.size)
                img.setImageBitmap(Bitmap.createScaledBitmap(bpm,  140, 140, false))
            }
        })
    }
}

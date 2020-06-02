package com.example.appnote.view.add

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.appnote.view.main.MainActivity
import com.example.appnote.model.Note
import com.example.appnote.R
import com.example.appnote.notify.AlarmScheduler
import com.example.appnote.notify.NotificationHelper
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.io.IOException
import java.util.*

class AddNotesActivity : AppCompatActivity() {

    private lateinit var viewModel: SaveNotesViewModel
    private var filePath: Uri? = null
    private var notificate: Boolean = false
    private var alarm: Boolean = false
    var id= ""

    companion object{
        const val PICK_IMAGE_REQUEST = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        viewModel = ViewModelProvider(this).get(SaveNotesViewModel::class.java)

        val intent = intent
        val bundle: Bundle? = intent.extras
        id = bundle?.getString("id") ?: ""

        if(id.isNotEmpty()){
           getNote()
       }

        saveNotesButton.setOnClickListener {

            if(alarm){
                AlarmScheduler.scheduleAlarmsForReminder(this@AddNotesActivity, fillNote())
            }
            if(notificate){
                NotificationHelper.createSampleDataNotification(this@AddNotesActivity, fillNote(), false)
            }

            if (id.isNotEmpty()){
                replaceNotes()
            }else{
                addNotes()
            }
        }

        seeMapIV.setOnClickListener   { showMap()             }
        constraint.setOnClickListener { showFileChooser()     }
        editDate.setOnClickListener   { displayPickerDialog() }
        editTime.setOnClickListener   { displayTimeDialog()   }

        switch1.setOnClickListener {
            switch1.setImageResource(R.drawable.ic_notifications_active_black_24dp)
            notificate = true
        }
        switchTime.setOnClickListener {
            switchTime.setImageResource(R.drawable.ic_alarm_on_black_24dp)
            alarm = true
        }

    }

    private fun showMap(){
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("address", editPlace.text.toString())
        startActivity(intent)
    }

    private fun getNote(){
        viewModel.getNote(id).observe(this, androidx.lifecycle.Observer {
           fillComponents(it)
        })
    }

    private fun fillComponents(note: Note){
        title_EditText.setText(note.title)
        description_EditText.setText(note.description)
        editPlace.setText(note.place)
        editDate.text = note.day.plus("/").plus(note.month).plus("/").plus(note.year)
        editTime.text = note.hour.plus(":").plus(note.minute)
    }

    private fun addNotes(){
        if (filePath != null){
            viewModel.addNote(fillNote(), filePath!!).observe(this, androidx.lifecycle.Observer {
                if (it){
                    backMainActivity()
                }else{
                    showToast()
                }
            })
        }
    }

    private fun replaceNotes(){
        viewModel.replaceNote(id, fillNote()).observe(this, androidx.lifecycle.Observer {
            if(it){
                backMainActivity()
            }else{
               showToast()
            }
        })
    }

    private fun fillNote(): Note {
        val date =  editDate.text.toString()
        val time = editTime.text.toString()

        return Note(
            "",
            title_EditText.text.toString(),
            description_EditText.text.toString(),
            date.substring(0, 2),
            date.substring(3, 5),
            date.substring(6, 10),
            time.substring(0, 2),
            time.substring(3,5),
            editPlace.text.toString(),
            false
        )
    }

    private fun backMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showFileChooser(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            filePath = data.data
            try {
                configImageAppearance()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun configImageAppearance(){
        viewFlipper.displayedChild = viewFlipper.displayedChild + 1
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
        imageNote.setImageBitmap(bitmap)
    }

    private fun displayPickerDialog(){
        val c = Calendar.getInstance()
        val myYear = c.get(Calendar.YEAR)
        val myMonth = c.get(Calendar.MONTH)
        val myDay = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            var newMonth: String = month.toString()
            var newDay: String = dayOfMonth.toString()
            if(month <= 9){
                newMonth = "0$month"
            }

            if (dayOfMonth <= 9){
                newDay = "0$dayOfMonth"
            }
            editDate.text  = newDay.plus("/").plus(newMonth).plus("/").plus(year)
        }, myYear, myMonth, myDay).show()
    }

    private fun displayTimeDialog(){
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            var newHour = hourOfDay.toString()
            var newMinute = minute.toString()

            if(hourOfDay < 10){
                newHour = "0$newHour"
            }
            if (minute < 10){
                newMinute = "0$newMinute"
            }

            editTime.text = newHour.plus(":").plus(newMinute)
        }, 0, 0, false).show()
    }

    private fun showToast(){
        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
    }
}

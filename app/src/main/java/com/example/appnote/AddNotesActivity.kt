package com.example.appnote

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.appnote.notify.AlarmScheduler
import com.example.appnote.notify.NotificationHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.io.IOException
import java.util.*

class AddNotesActivity : AppCompatActivity() {

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
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


        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        val intent = intent
        val bundle :Bundle? = intent.extras
        id = bundle?.getString("id") ?: ""

       if(id.isNotEmpty()){
           fieldComponents()
       }

        saveNotesButton.setOnClickListener {
            if (id.isNotEmpty()){
                replaceNotesInFirebase()
            }else{
                addNotesToFirebase()
            }
            if(alarm){
                AlarmScheduler.scheduleAlarmsForReminder(
                    this,
                    setNotesData())
            }
            if(notificate){
                NotificationHelper.createSampleDataNotification(this@AddNotesActivity, setNotesData(), false)
            }
        }

        constraint.setOnClickListener {
            showFileChooser()
        }

        seeMapIV.setOnClickListener {
            showMap()
        }

        switch1.setOnClickListener {
            switch1.setImageResource(R.drawable.ic_notifications_active_black_24dp)
            notificate = true
        }
        switchTime.setOnClickListener {
            switchTime.setImageResource(R.drawable.ic_alarm_on_black_24dp)
            alarm = true
        }

        editDate.setOnClickListener {
            dpd()
        }

        editTime.setOnClickListener{
            displayTimeDialog()
        }
    }

    private fun showMap(){
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("address", editPlace.text.toString())
        startActivity(intent)
    }

    private fun fieldComponents(){

        firebaseFirestore.collection("notes")
            .document(id)
            .get()
            .addOnSuccessListener { doc ->
               if (doc != null) {
                    doc.getString("title")?.let {
                        title_EditText.setText(it)
                    }
                    doc.getString("description")?.let {
                        description_EditText.setText(it)
                    }
                    doc.getString("place")?.let {
                        editPlace.setText(it)
                    }

                    var day = ""
                    var month = ""
                    var year = ""
                    doc.getString("year")?.let {
                        year = it
                    }

                    doc.getString("month")?.let {
                        month = it
                    }

                    doc.getString("day")?.let {
                        day = it
                    }

                    editDate.text = day.plus("/").plus(month).plus("/").plus(year)


                    var hour = ""
                    var min = ""

                    doc.getString("hour")?.let {
                        hour = it
                    }

                    doc.getString("minute")?.let{
                        min = it
                    }
                    editTime.text = hour.plus(":").plus(min)
                }
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                Log.d("what", "get failed with", exception)
            }
    }


    private fun addNotesToFirebase(){


        firebaseFirestore.collection("notes")
            .add(fieldNote())
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "File uploaded", Toast.LENGTH_LONG).show()
                backMainActivity()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

        /*val imageRef = storageReference.child(UUID.randomUUID().toString())
        imageRef.putFile(filePath!!)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Image uploaded", Toast.LENGTH_LONG).show()
                backMainActivity()
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext, "Failed image", Toast.LENGTH_LONG).show()
            }*/
    }

    private fun replaceNotesInFirebase(){
        firebaseFirestore.collection("notes")
            .document(id)
            .set(fieldNote())
            .addOnSuccessListener {documentReference ->
                Toast.makeText(applicationContext, "File uploaded", Toast.LENGTH_LONG).show()
                backMainActivity()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

    }
    private fun fieldNote(): Note{
        val date =  editDate.text.toString()
        val time = editTime.text.toString()

        return Note(
            title_EditText.text.toString(),
            description_EditText.text.toString(),
            configDay(date),
            configMonth(date),
            configYear(date),
            configHour(time),
            configMinute(time),
            editPlace.text.toString()

        )
    }

    private fun setNotesData(): Notes{
        val notes = Notes()
        notes.title = title_EditText.text.toString()
        notes.description = description_EditText.text.toString()

        val date =  editDate.text.toString()
        notes.day = configDay(date)
        notes.month = configMonth(date)
        notes.year = configYear(date)

        val time = editTime.text.toString()
        notes.hour = configHour(time)
        notes.minute = configMinute(time)

        notes.place = editPlace.text.toString()
        notes.image = filePath.toString()
        return notes
    }

    private fun configDay(date: String): String{
        return date.substring(0, 2)
    }

    private fun configMonth(date: String): String{
        return date.substring(3, 5)
    }

    private fun configYear(date: String): String{
        return date.substring(6, 10)
    }

    private fun configHour(time: String): String{
        return time.substring(0, 2)
    }

    private fun configMinute(time: String): String{
        return time.substring(3,5)
    }

    private fun backMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showFileChooser(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
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

    private fun dpd(){
        val c = Calendar.getInstance()
        val myYear = c.get(Calendar.YEAR)
        val myMonth = c.get(Calendar.MONTH)
        val myDay = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            var newMonth: String = month.toString()
            var newDay: String = dayOfMonth.toString()
            if(month <= 9){
                newMonth = "0$month"
            }

            if (dayOfMonth <= 9){
                newDay = "0$dayOfMonth"
            }
            editDate.text  = newDay.plus("/").plus(newMonth).plus("/").plus(year)
        }, myYear, myMonth, myDay)
        return dpd.show()
    }

    private fun displayTimeDialog(){
        val hour  = 0
        val min = 0
        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            var newHour = hourOfDay.toString()
            var newMinute = minute.toString()


            if(hourOfDay < 10){
                newHour = "0$newHour"
            }
            if (minute < 10){
                newMinute = "0$newMinute"
            }

            editTime.text = newHour.plus(":").plus(newMinute)
        }, hour, min, false)
        tpd.show()
    }
}

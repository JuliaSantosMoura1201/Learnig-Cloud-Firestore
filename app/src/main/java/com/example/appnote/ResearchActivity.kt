package com.example.appnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_research.*
import java.lang.Exception

class ResearchActivity : AppCompatActivity() {

    private var listOfItems = arrayOf("id", "title")
    var type = ""
    var idDialog = 0

    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research)


        firebaseFirestore = FirebaseFirestore.getInstance()

        spinner!!.onItemSelectedListener
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    type = parent.getItemAtPosition(position).toString()
                }
            }

        }

        btn_search.setOnClickListener { defineResearchType() }

        cardViewResearch.setOnClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("id", idDialog)
            startActivity(intent)
        }
    }


    private fun defineResearchType(){
        cardViewResearch.visibility = View.GONE

        if(type == "title"){
            makeResearchTitle()
            return
        }

        makeResearchId()
    }
    private fun makeResearchTitle(){
        firebaseFirestore.collection("notes")
            .whereEqualTo("title", edtNote.text.toString())
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(firebaseFirestoreException != null){
                return@addSnapshotListener
            }

            for (doc in querySnapshot!!){
                cardViewResearch.visibility = View.VISIBLE
                doc.getString("title")?.let {
                    titleResearchTV.text = it
                }
                doc.getString("description")?.let {
                    descResearchTV.text = it
                }
                doc.get("id")?.let {
                    idResearchTV.text = it.toString()
                }
            }
        }
    }

    private fun makeResearchId(){

        var note = 1

        try {
            note = edtNote.text.toString().toInt()
        }catch (e: Exception){
            showToast()
        }

        firebaseFirestore.collection("notes")
            .whereEqualTo("id", note)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    return@addSnapshotListener
                }

                for (doc in querySnapshot!!){
                    cardViewResearch.visibility = View.VISIBLE
                    doc.getString("title")?.let {
                        titleResearchTV.text = it
                    }
                    doc.getString("description")?.let {
                        descResearchTV.text = it
                    }
                    doc.get("id")?.let {
                        idResearchTV.text = it.toString()
                    }
                }
            }
    }

    private fun showToast(){
        Toast.makeText(this, "Esse item é inválido ou não existe\nPor favor tente novamente", Toast.LENGTH_SHORT).show()
    }
}

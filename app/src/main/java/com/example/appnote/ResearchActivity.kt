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

class ResearchActivity : AppCompatActivity() {

    private var listOfItems = arrayOf("title", "description")
    var type = ""
    private var idDialog = ""

    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research)

        firebaseFirestore = FirebaseFirestore.getInstance()

        configSpinner()

        btn_search.setOnClickListener { makeResearch() }

        cardViewResearch.setOnClickListener {goToAddActivity()}
    }

    private fun configSpinner(){
        spinner!!.onItemSelectedListener
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinnerAction()
    }

    private fun spinnerAction(){
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    type = parent.getItemAtPosition(position).toString()
                }
            }

        }
    }

    private fun makeResearch(){
        cardViewResearch.visibility = View.GONE

        firebaseFirestore.collection("notes").whereEqualTo(type, edtNote.text.toString())
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(firebaseFirestoreException != null){
                showToast()
                return@addSnapshotListener
            }
                if (querySnapshot == null || querySnapshot.isEmpty){
                    showToast()
                    return@addSnapshotListener
                }

            for (doc in querySnapshot){
                cardViewResearch.visibility = View.VISIBLE
                idDialog = doc.id
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
        Toast.makeText(this, "This item does not exists or is invalid, please try again", Toast.LENGTH_SHORT).show()
    }

    private fun goToAddActivity(){
        val intent = Intent(this, AddNotesActivity::class.java)
        intent.putExtra("id", idDialog)
        startActivity(intent)
    }
}

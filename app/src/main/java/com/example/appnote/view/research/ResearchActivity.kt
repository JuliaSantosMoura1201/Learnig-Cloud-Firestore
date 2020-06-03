package com.example.appnote.view.research

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appnote.R
import com.example.appnote.model.Note
import com.example.appnote.view.save.SaveNotesActivity
import kotlinx.android.synthetic.main.activity_research.*

class ResearchActivity : AppCompatActivity() {

    private var listOfItems = arrayOf("title", "description")
    var type = ""
    private var idDialog = ""

    private lateinit var viewModel: ResearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research)

        viewModel = ViewModelProvider(this).get(ResearchViewModel::class.java)

        configSpinner()

        btn_search.setOnClickListener {validateResearch() }

        cardViewResearch.setOnClickListener {goToAddActivity()}
    }

    private fun validateResearch(){
        if(viewModel.isResearchValid(edtNote.text.toString())){
            makeResearch()
        }else{
            showToast(R.string.research_empty)
        }
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

    private fun makeResearch() {
        cardViewResearch.visibility = View.GONE

        viewModel.research(type, edtNote.text.toString()).observe(this, Observer {

            if(it.state != null){
                configAppearance(it)
            }else{
                showToast(R.string.invalid_research)
            }
        })

    }

    private fun configAppearance(note: Note){
        cardViewResearch.visibility = View.VISIBLE
        descResearchTV.text = note.description
        titleResearchTV.text = note.title
        idDialog = note.id.toString()
        configIcon(note.state!!)
    }

    private fun configIcon(state: Boolean){
        if (state) {
            idResearchTV.setButtonDrawable(R.drawable.ic_check_box_black_24dp)
        } else {
            idResearchTV.setButtonDrawable(R.drawable.ic_check_box_outline_blank_black_24dp)
        }
    }

    private fun showToast(message: Int){
        Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
    }

    private fun goToAddActivity(){
        val intent = Intent(this, SaveNotesActivity::class.java)
        intent.putExtra("id", idDialog)
        startActivity(intent)
    }
}

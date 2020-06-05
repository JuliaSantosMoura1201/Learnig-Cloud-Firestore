package com.example.appnote.view.singin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appnote.R
import com.example.appnote.model.User
import com.example.appnote.view.main.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_account.*
import java.io.IOException

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: CreateAccountViewModel
    private var filePath: Uri? = null
    companion object{
        const val PICK_IMAGE_REQUEST = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configSharedPreferences()
        setContentView(R.layout.activity_create_account)

        viewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)

        btn_sing_in.setOnClickListener{validateFields()}

        ivProfilePhoto.setOnClickListener { showFileChooser() }
    }

    private fun validateFields(){
        if(viewModel.isEmailValid(edit_email.text.toString())){
            if(viewModel.isPasswordValid(edit_password.text.toString())){
                singIn()
            }else{
                showToast(R.string.password_too_small)
            }
        }else{
            showToast(R.string.invalid_email)
        }
    }

    private fun singIn(){
        viewModel.singInState(edit_email.text.toString(), edit_password.text.toString())
            .observe(this, Observer {
                if(it){
                    saveUser()
                }else{
                    showToast(R.string.general_error)
                }
            } )
    }

    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(text: Int){
        Toast.makeText(this, getString(text), Toast.LENGTH_SHORT).show()
    }

    private fun configSharedPreferences(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        val themeKey = "currentTheme"

        when(sharedPreferences.getString(themeKey, "pattern")){
            "optionYellow" -> theme.applyStyle(R.style.OverlayThemeYellow, true)
            "optionPurple" -> theme.applyStyle(R.style.OverlayThemePurple, true)
            "pattern" -> theme.applyStyle(R.style.OverlayThemeLine, true)
        }
    }

    private fun saveUser(){

        val user = User(editName.text.toString(), edit_email.text.toString())
        viewModel.saveUser(user).observe(this, Observer {
            if(it.isNotEmpty()){
                saveProfileImage(it)
            }
        })

    }

    private fun saveProfileImage(id: String){
        if(filePath != null){
            viewModel.saveProfilePhoto(id, filePath!!).observe(this, Observer {
                if (it){
                    goToMain()
                }
            })
        }else{
            showToast(R.string.image_empty)
        }
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
        val picasso = Picasso.get()
        picasso.load(filePath)
            .into(ivProfilePhoto)
    }
}

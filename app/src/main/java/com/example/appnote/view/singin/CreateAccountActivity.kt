package com.example.appnote.view.singin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appnote.R
import com.example.appnote.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: CreateAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configSharedPreferences()
        setContentView(R.layout.activity_create_account)

        viewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)

        btn_sing_in.setOnClickListener{validateFields()}

    }

    private fun validateFields(){
        if(viewModel.isEmailValid(edit_username.text.toString())){
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
        viewModel.singInState(edit_username.text.toString(), edit_password.text.toString())
            .observe(this, Observer {
                if(it){
                    goToMain()
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
}

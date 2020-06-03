package com.example.appnote.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appnote.view.singin.CreateAccountActivity
import com.example.appnote.R
import com.example.appnote.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        isLogged()
        login.setOnClickListener { validateFields() }

        underlineComponent()

        txt_create_account.setOnClickListener {
            goToCreateAccount()
        }

    }

    private fun validateFields(){
        if(viewModel.isEmailValid(username.text.toString())){
            if(viewModel.isPasswordValid(password.text.toString())){
                login()
            }else{
                showToast(R.string.invalid_password)
            }
        }else{
            showToast(R.string.invalid_email)
        }
    }

    private fun isLogged(){
        viewModel.isLogged().observe(this, Observer {
            if(it){
                goToMain()
            }
        })
    }

    private fun login(){
        viewModel.login(username.text.toString(), password.text.toString()).observe(this, Observer {
            if(it){
                goToMain()
            }else{
                showToast(R.string.general_error)
            }
        })
    }
    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToCreateAccount(){
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    private fun underlineComponent() {
        val content = SpannableString(getString(R.string.txtCreateAccount))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        txt_create_account.text = content
    }

    private fun showToast(text: Int){
        Toast.makeText(this, getString(text), Toast.LENGTH_SHORT).show()
    }
}

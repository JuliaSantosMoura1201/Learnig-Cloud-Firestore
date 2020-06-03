package com.example.appnote.view.singin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appnote.R
import com.example.appnote.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: CreateAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    private fun showToast(text: Int){
        Toast.makeText(this, getString(text), Toast.LENGTH_SHORT).show()
    }

}

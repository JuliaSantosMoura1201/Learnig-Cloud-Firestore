package com.example.appnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import com.example.appnote.notify.CreateAccountActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        underlineComponent()

        if(user.currentUser != null){
            goToMain()
        }else{
            login.setOnClickListener {
                user.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful){
                            goToMain()
                        }else{
                            Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        txt_create_account.setOnClickListener {
            goToCreateAccount()
        }

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
}

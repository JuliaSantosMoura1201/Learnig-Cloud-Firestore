package com.example.appnote.notify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import com.example.appnote.MainActivity
import com.example.appnote.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    private val user: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        btn_sing_in.setOnClickListener{
            user.createUserWithEmailAndPassword(edit_username.text.toString(), edit_password.text.toString())
                .addOnCompleteListener(this) {task ->
                    if(task.isSuccessful){
                        user.signInWithEmailAndPassword(edit_username.text.toString(), edit_password.text.toString())
                            .addOnCompleteListener(this){task->
                                if(task.isSuccessful){
                                    goToMain()
                                }else{
                                    Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else{
                        Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}

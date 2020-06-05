package com.example.appnote.view.updateProfile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appnote.R
import com.example.appnote.view.login.LoginActivity
import com.example.appnote.view.main.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_update_profile.*
import java.io.IOException

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: UpdateProfileViewModel
    private var filePath : Uri? = null

    companion object{
        const val PICK_IMAGE_REQUEST = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        viewModel = ViewModelProvider(this).get(UpdateProfileViewModel::class.java)

        getNote()
        getImage()

        btnUpdateProfile.setOnClickListener {

            updateUser()
        }
        ivUpdateProfilePicture.setOnClickListener { showFileChooser() }
    }


    private fun getNote(){
        viewModel.getUser().observe(this, Observer {
            edtUpdateName.setText(it.name)
        })
    }

    private fun getImage(){
        viewModel.getImage().observe(this, Observer {
            val bpm = BitmapFactory.decodeByteArray(it, 0, it.size)
            ivUpdateProfilePicture.setImageBitmap(Bitmap.createScaledBitmap(bpm,  200, 200, false))
        })
    }

    private fun updateUser(){
        viewModel.updateUser(edtUpdateName.text.toString()).observe(this, Observer {
            if (it){
                if(filePath != null){
                    viewModel.updateProfilePic(filePath!!).observe(this, Observer {imageOk ->
                        if(imageOk){
                            if(resetPassword.isChecked){
                                resetPassword()
                            }else{
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                    })
                }else{
                    if(resetPassword.isChecked){
                        resetPassword()
                    }else{
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        })
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
            .into(ivUpdateProfilePicture)
    }

    private fun resetPassword(){
        viewModel.updatePassword().observe(this, Observer {
            if(it){
                viewModel.logOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        })
    }
}

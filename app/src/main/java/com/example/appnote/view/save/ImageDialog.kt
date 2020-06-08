package com.example.appnote.view.save

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.appnote.R
import com.squareup.picasso.Picasso

class ImageDialog: DialogFragment() {

    internal lateinit var parentActivity: FragmentActivity
    var myWidth = 0
    var myHeight = 0
    var myFilePath : Uri? = null
    var myByteArray: ByteArray? = null

    companion object{
        fun newInstance(activity: FragmentActivity, width: Int, height: Int, filePath: Uri?, byteArray: ByteArray?): DialogFragment {
            val dialog = ImageDialog()
            dialog.parentActivity = activity
            dialog.myWidth = width
            dialog.myHeight = height
            dialog.myFilePath = filePath
            dialog.myByteArray = byteArray
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val myView = View.inflate(context, R.layout.image_dialog, null)

        val img = myView.findViewById<ImageView>(R.id.chosen_image)
        img.layoutParams.width = myWidth
        img.layoutParams.height = myHeight
        if(myFilePath != null){
            val picasso = Picasso.get()
            picasso.load(myFilePath).into(img)
        }else if( myByteArray != null){
            val bpm = BitmapFactory.decodeByteArray(myByteArray, 0, myByteArray!!.size)
            img.setImageBitmap(Bitmap.createScaledBitmap(bpm, myWidth, myHeight, false))
        }
        val alertDialog = AlertDialog.Builder(parentActivity)
        alertDialog.setView(myView)
        return alertDialog.create()
    }

}
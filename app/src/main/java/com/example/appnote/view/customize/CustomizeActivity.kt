package com.example.appnote.view.customize

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appnote.R
import com.example.appnote.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_customize.*

class CustomizeActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    private var option = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configSharedPreferences()

        setContentView(R.layout.activity_customize)

        colorPattern.setOnClickListener {option = "pattern"}

        colorOption1.setOnClickListener {option = "optionPurple"}

        colorOption2.setOnClickListener {option = "optionYellow"}

        btn_change_color.setOnClickListener {
            saveAndGoToMain()
        }

    }

    private fun configSharedPreferences(){
       sharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)

        when(sharedPreferences.getString(themeKey, "pattern")){
            "optionYellow" -> theme.applyStyle(R.style.OverlayThemeYellow, true)
            "optionPurple" -> theme.applyStyle(R.style.OverlayThemePurple, true)
            "pattern" -> theme.applyStyle(R.style.OverlayThemeLine, true)
        }
    }
    private fun  restartActivity(){
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }

    private fun saveAndGoToMain(){
        sharedPreferences.edit().putString(themeKey, option).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

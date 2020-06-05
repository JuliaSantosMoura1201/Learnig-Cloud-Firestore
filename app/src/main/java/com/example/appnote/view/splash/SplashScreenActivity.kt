package com.example.appnote.view.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.appnote.R
import com.example.appnote.view.login.LoginActivity
import com.example.appnote.view.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configSharedPreferences()
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)

        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)

        Handler().postDelayed({
            viewModel.isLogged().observeForever {
                if (it){
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    finish()
                }
            }


        }, 4000)
    }

    private fun configSharedPreferences(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        val themeKey = "currentTheme"

        when(sharedPreferences.getString(themeKey, "pattern")){
            "optionYellow" -> theme.applyStyle(R.style.OverlayThemeYellowMain, true)
            "optionPurple" -> theme.applyStyle(R.style.OverlayThemePurpleMain, true)
            "pattern" -> theme.applyStyle(R.style.OverlayThemeLineMain, true)
        }
    }
}

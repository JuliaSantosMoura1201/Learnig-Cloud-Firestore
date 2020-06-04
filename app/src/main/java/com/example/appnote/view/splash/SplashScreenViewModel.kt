package com.example.appnote.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.appnote.data.splash.SplashScreenRepository

class SplashScreenViewModel: ViewModel() {

    private val repository = SplashScreenRepository()
    fun isLogged(): LiveData<Boolean> {
        return repository.isLogged()
    }
}
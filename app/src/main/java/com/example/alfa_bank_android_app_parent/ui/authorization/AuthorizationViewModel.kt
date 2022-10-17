package com.example.alfa_bank_android_app_parent.ui.authorization

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.alfa_bank_android_app_parent.data.PreferencesImpl

class AuthorizationViewModel(application: Application):AndroidViewModel(application) {
    val preferences = PreferencesImpl(application.applicationContext)
}
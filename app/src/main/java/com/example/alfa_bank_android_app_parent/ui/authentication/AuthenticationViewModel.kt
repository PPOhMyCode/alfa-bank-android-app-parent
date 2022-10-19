package com.example.alfa_bank_android_app_parent.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class AuthenticationViewModel(application: Application):AndroidViewModel(application) {
    var length = MutableLiveData<Int>()
}
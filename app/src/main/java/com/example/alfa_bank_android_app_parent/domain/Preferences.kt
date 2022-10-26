package com.example.alfa_bank_android_app_parent.domain

import android.content.Context

abstract class Preferences(context: Context) {
    abstract var isUserLogged : Boolean
    abstract var userPinCode: Int
}
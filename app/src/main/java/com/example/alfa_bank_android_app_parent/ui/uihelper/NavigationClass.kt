package com.example.alfa_bank_android_app_parent.ui.uihelper

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.alfa_bank_android_app_parent.MainActivity
import com.example.alfa_bank_android_app_parent.ui.authorization.AuthorizationActivity

class NavigationClass {
    fun goToAuthorizationActivity(context: Context, params: List<ExtraParam>?=null) {
        goToActivity(context, AuthorizationActivity(),params)
    }

    fun goToMainActivity(context: Context, params: List<ExtraParam>?=null) {
        goToActivity(context, MainActivity(),params)
    }

    private fun goToActivity(
        startActivityContext: Context, finishActivity: AppCompatActivity,
        params: List<ExtraParam>?=null
    ) {
        val intent = Intent(startActivityContext, finishActivity::class.java)
        params?.let {
            for (param in it)
                intent.putExtra(param.key, param.value)
        }
        startActivityContext.startActivity(intent)
    }
}

data class ExtraParam(var key: String, var value: String)
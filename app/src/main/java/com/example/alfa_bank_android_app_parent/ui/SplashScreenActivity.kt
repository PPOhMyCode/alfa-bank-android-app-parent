package com.example.alfa_bank_android_app_parent.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alfa_bank_android_app_parent.MainActivity
import com.example.alfa_bank_android_app_parent.R
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        this.supportActionBar?.hide()
        Timer().schedule(1000){
            goToMainActivity()
            finish()
        }
        //finish()
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}
package com.example.alfa_bank_android_app_parent.ui.splashscreenactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.alfa_bank_android_app_parent.MainActivity
import com.example.alfa_bank_android_app_parent.R
import com.example.alfa_bank_android_app_parent.ui.authorization.AuthorizationActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var viewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        this.supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]
        Timer().schedule(1000){
            if(viewModel.preferences.isUserLogged){
                goToMainActivity()
            }else{
                goToAuthorizationActivity()
            }

            finish()
        }
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    private fun goToAuthorizationActivity(){
        val intent = Intent(this, AuthorizationActivity::class.java)
        this.startActivity(intent)
    }
}
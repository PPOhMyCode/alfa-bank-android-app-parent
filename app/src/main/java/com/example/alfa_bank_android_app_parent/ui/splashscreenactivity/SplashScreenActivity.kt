package com.example.alfa_bank_android_app_parent.ui.splashscreenactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.alfa_bank_android_app_parent.R
import com.example.alfa_bank_android_app_parent.ui.authentication.AuthenticationActivity
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
                val intent = AuthenticationActivity.newIntentAuthentication(application)
                application.startActivity(intent)
            }else{
                val intent = Intent(applicationContext,AuthorizationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                application.startActivity(intent)
            }
            finish()
        }
    }

}
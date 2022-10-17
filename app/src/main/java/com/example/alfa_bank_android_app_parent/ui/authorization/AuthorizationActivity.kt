package com.example.alfa_bank_android_app_parent.ui.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.alfa_bank_android_app_parent.MainActivity
import com.example.alfa_bank_android_app_parent.R

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthorizationViewModel
    private lateinit var buttonAuthorization: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[AuthorizationViewModel::class.java]
        setContentView(R.layout.activity_authorization)
        idInitialize()
        initializeOnClickListener()
    }

    private fun idInitialize() {
        buttonAuthorization = findViewById(R.id.buttonAuthorization)
    }

    private fun initializeOnClickListener() {
        buttonAuthorization.setOnClickListener {
            viewModel.preferences.isUserLogged = true
            goToMainActivity()
            finish()
        }
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}
package com.example.alfa_bank_android_app_parent.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfa_bank_android_app_parent.MainActivity
import com.example.alfa_bank_android_app_parent.R
import com.example.alfa_bank_android_app_parent.domain.entiies.AuthenticationItemsForAdapter
import com.example.alfa_bank_android_app_parent.domain.entiies.PinClass
import com.example.alfa_bank_android_app_parent.ui.adapters.AuthenticationCardAdapter
import com.example.alfa_bank_android_app_parent.ui.adapters.PinCodeAdapter
import com.example.alfa_bank_android_app_parent.ui.authorization.AuthorizationActivity

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var buttonsRecyclerView: RecyclerView
    private lateinit var lengthPinRecyclerView: RecyclerView
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var pin: PinClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        viewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]

        pin = PinClass()
        this.supportActionBar?.hide()
        idInitialize()
        initializeRecyclerView()
    }

    private fun idInitialize() {
        buttonsRecyclerView = findViewById(R.id.buttonsRecyclerView)
        lengthPinRecyclerView = findViewById(R.id.lengthPinRecyclerView)
    }

    private fun initializeRecyclerView() {
        initializeButtonsRecyclerView()
        initializeLengthPinRecyclerView()
    }

    private fun initializeLengthPinRecyclerView() {
        val linearLayoutManager = GridLayoutManager(applicationContext, 4)
        val pinCardAdapter = PinCodeAdapter(0)
        viewModel.length.observe(this, Observer {
            pinCardAdapter.length = it
            pinCardAdapter.notifyDataSetChanged()
        }
        )
        with(lengthPinRecyclerView) {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = pinCardAdapter
        }
    }

    private fun initializeButtonsRecyclerView() {
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        val authenticationItemsForAdapter = AuthenticationItemsForAdapter()
        with(authenticationItemsForAdapter) {
            for (number in 1..9)
                addNumber(AuthenticationItemsForAdapter.ItemNumber(number) {
                    pin.addNumber(number)
                    viewModel.length.value = pin.getPin().length
                    if(pin.getPin().length==4){
                        goToMainActivity()
                    }
                })
            addString(AuthenticationItemsForAdapter.ItemString("выход") {
                goToAuthorizationActivity()
            })
            addNumber(AuthenticationItemsForAdapter.ItemNumber(0) {
                pin.addNumber(0)
                viewModel.length.value = pin.getPin().length
                if(pin.getPin().length==4){
                    goToMainActivity()
                }
            })
            addImage(
                AuthenticationItemsForAdapter.ItemImage(R.drawable.ic_baseline_backspace_24) {
                    pin.removeNumber()
                    viewModel.length.value = pin.getPin().length
                }
            )
        }

        val cardAdapter = AuthenticationCardAdapter(authenticationItemsForAdapter) {

        }

        with(buttonsRecyclerView) {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = cardAdapter
        }
    }

    private fun goToAuthorizationActivity() {
        val intent = Intent(this, AuthorizationActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finish()
    }
}
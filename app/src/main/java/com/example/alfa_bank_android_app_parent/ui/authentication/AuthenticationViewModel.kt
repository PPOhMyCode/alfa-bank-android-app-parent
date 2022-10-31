package com.example.alfa_bank_android_app_parent.ui.authentication

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.alfa_bank_android_app_parent.MainActivity
import com.example.alfa_bank_android_app_parent.R
import com.example.alfa_bank_android_app_parent.data.PreferencesImpl
import com.example.alfa_bank_android_app_parent.domain.entiies.AuthenticationItemsForAdapter
import com.example.alfa_bank_android_app_parent.domain.entiies.PinClass
import com.example.alfa_bank_android_app_parent.ui.adapters.AuthenticationCardAdapter
import com.example.alfa_bank_android_app_parent.ui.authorization.AuthorizationActivity

class AuthenticationViewModel(var application2: Application) : AndroidViewModel(application2) {
    var length = MutableLiveData<Int>()

    var pinClass:PinClass? = null
    val preferences = PreferencesImpl(application2.applicationContext)
    var funAfterPinWasEntered: ((s: String?) -> Unit)? = null

    private fun getPinLength(): Int {

        return pinClass?.getPin()?.length?:0

    }


    fun loadItemsForAdapter(
        authenticationItemsForAdapter: AuthenticationItemsForAdapter,
        biometricPrompt: BiometricPrompt,
        promptInfo: BiometricPrompt.PromptInfo,
        mode: String,
    ) {
        pinClass?.let { pin ->
            val image = getImageForAdapter(biometricPrompt, promptInfo, mode)

            with(authenticationItemsForAdapter) {
                for (number in 1..9)
                    addNumber(AuthenticationItemsForAdapter.ItemNumber(number) {
                        pin.addNumber(number)
                        //adapter.notifyItemChanged(11)
                        length.value = pin.getPin().length
                        if (pin.getPin().length == 4) {
                            funAfterPinWasEntered?.let { it(pin.getPin()) }
                        }

                    })
                addString(AuthenticationItemsForAdapter.ItemString("выход") {
                    preferences.isUserLogged = false
                    goToAuthorizationActivity()
                })
                addNumber(AuthenticationItemsForAdapter.ItemNumber(0) {
                    pin.addNumber(0)
                    //adapter.notifyItemChanged(11)
                    length.value = pin.getPin().length
                    if (pin.getPin().length == 4) {
                        funAfterPinWasEntered?.let { it(pin.getPin()) }
                    }
                })
                addImage(image)
            }
        }
    }

    private fun getImageForAdapter(
        biometricPrompt: BiometricPrompt,
        promptInfo: BiometricPrompt.PromptInfo,
        mode: String
    ): AuthenticationItemsForAdapter.ItemImage {
        if (getPinLength() >= 1
            || mode == AuthenticationActivity.INPUT_FIRST_TIME_MODE
            || mode == AuthenticationActivity.INPUT_SECOND_TIME_MODE)
            pinClass?.let { pin ->
                return AuthenticationItemsForAdapter.ItemImage(R.drawable.ic_baseline_backspace_24) {
                    pin.removeNumber()
                    length.value = pin.getPin().length
                }
            }

        return AuthenticationItemsForAdapter.ItemImage(R.drawable.ic_baseline_fingerprint_24) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    fun goToAuthorizationActivity() {
        val intent = Intent(application2.applicationContext, AuthorizationActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application2.applicationContext.startActivity(intent)

    }

    fun goToMainActivity() {
        val intent = Intent(application2.applicationContext, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application2.applicationContext.startActivity(intent)

    }
}
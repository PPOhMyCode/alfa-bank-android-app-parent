package com.example.alfa_bank_android_app_parent.domain.entiies

import android.widget.ImageView
import com.example.alfa_bank_android_app_parent.R
import com.example.alfa_bank_android_app_parent.ui.adapters.AuthenticationCardAdapter

class PinClass(
    var circles: List<ImageView>,

    private var _pin: StringBuilder = StringBuilder()
) {
    var adapter: (()->Unit)?=null

    fun addNumber(number: Int) {
        if (_pin.count() < 4) {
            val index=_pin.count()
            circles[index].setImageResource(R.drawable.ic_baseline_full_circle_24)
            _pin.append(number.toString())
            adapter?.invoke()
        }
    }

    fun getPin(): String {
        return _pin.toString()
    }

    fun removeNumber() {
        if (_pin.count() - 1 >= 0) {
            val index=_pin.count() - 1
            _pin.deleteCharAt(index)
            circles[index].setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
            adapter?.invoke()
        }
    }

    fun removePin() {
        _pin.clear()
        for( circle in circles){
            circle.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
        }
        adapter?.invoke()
    }

    fun addAllPinCode(){
        for( circle in circles){
            circle.setImageResource(R.drawable.ic_baseline_full_circle_24)
        }
    }

}
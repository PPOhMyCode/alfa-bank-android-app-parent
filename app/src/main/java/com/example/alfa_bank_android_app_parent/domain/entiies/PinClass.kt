package com.example.alfa_bank_android_app_parent.domain.entiies

class PinClass(private var _pin: StringBuilder = StringBuilder() ) {

    fun addNumber(number: Int) {
        _pin.append(number.toString())
    }

    fun getPin(): String {
        return _pin.toString()
    }

    fun removeNumber() {
        if(_pin.count()-1>=0)
            _pin.deleteCharAt(_pin.count()-1)
    }

}
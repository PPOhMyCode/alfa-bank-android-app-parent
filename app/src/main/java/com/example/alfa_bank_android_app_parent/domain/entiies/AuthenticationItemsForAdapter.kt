package com.example.alfa_bank_android_app_parent.domain.entiies

class AuthenticationItemsForAdapter {
    private var _authenticationItemsForAdapter: MutableList<Any> = mutableListOf()

    fun addImage(itemImage: ItemImage) {
        _authenticationItemsForAdapter.add(itemImage)
    }

    fun addNumber(number: ItemNumber) {
        _authenticationItemsForAdapter.add(number)
    }

    fun addString(str: ItemString) {
        _authenticationItemsForAdapter.add(str)
    }

    fun getItemsForAdapter(): List<Any> {
        return _authenticationItemsForAdapter
    }

    class ItemNumber(var number: Int,var f: () -> Unit)
    class ItemString(var str: String,var f: () -> Unit)
    class ItemImage(var idImage: Int,var f: () -> Unit)
}






package com.example.alfa_bank_android_app_parent.ui.uihelper

import androidx.recyclerview.widget.RecyclerView

class InitializeRecyclerViewClass<T : RecyclerView.ViewHolder?> {
    fun setParametersToRecyclerView (
        recyclerView: RecyclerView,
        setLayoutManager: RecyclerView.LayoutManager,
        setAdapter: RecyclerView.Adapter<T>
    ) {
        with(recyclerView) {
            layoutManager = setLayoutManager
            setHasFixedSize(true)
            adapter = setAdapter
        }
    }
}
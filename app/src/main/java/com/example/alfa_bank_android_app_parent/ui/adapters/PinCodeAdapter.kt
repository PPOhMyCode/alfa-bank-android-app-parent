package com.example.alfa_bank_android_app_parent.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alfa_bank_android_app_parent.R


class PinCodeAdapter(var pinLength: Int) : RecyclerView.Adapter<PinCodeAdapter.ItemHolder>() {
    var views = mutableListOf<View>()
    var functions = mutableListOf<() -> Unit>()
    //var item: (() -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        Log.d("VIEWHOLDER", views.size.toString())
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_authentication_length_pin, parent, false)
        views.add(itemHolder)

        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemViewType(position: Int): Int {
        if (position < pinLength) {
            return 0
        }
        return CARD_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        functions.add{
            ViewCompat.animate(holder.itemView)
                .translationX(50f)
                .translationY(100f)
                .setDuration(1000)
                .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 50
        }
        if (position < pinLength) {
            holder.image.setImageResource(R.drawable.ic_baseline_full_circle_24)
        } else {
            holder.image.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
        }
    }

    companion object {
        const val CARD_VIEW_TYPE = 1
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.itemImageView)
        //var f: (()->Unit)?=null
    }
}
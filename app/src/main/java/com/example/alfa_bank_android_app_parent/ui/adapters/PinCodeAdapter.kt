package com.example.alfa_bank_android_app_parent.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfa_bank_android_app_parent.R


class PinCodeAdapter(var length:Int) : RecyclerView.Adapter<PinCodeAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_authentication_length_pin, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemViewType(position: Int): Int {
        if(position<length){
            return 0
        }
        return CARD_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if(position<length){
            holder.image.setImageResource(R.drawable.ic_baseline_full_circle_24)
        }else{
            holder.image.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
        }
    }

    companion object {
        const val CARD_VIEW_TYPE = 1
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.itemImageView)
    }
}
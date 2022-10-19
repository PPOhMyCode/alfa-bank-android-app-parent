package com.example.alfa_bank_android_app_parent.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfa_bank_android_app_parent.R
import com.example.alfa_bank_android_app_parent.domain.entiies.AuthenticationItemsForAdapter

class AuthenticationCardAdapter(
    var authenticationItemsForAdapter: AuthenticationItemsForAdapter,
    var onItemClickListener: (() -> Unit)? = null
) : RecyclerView.Adapter<AuthenticationCardAdapter.ItemHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_authentication, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return authenticationItemsForAdapter.getItemsForAdapter().size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val obj = authenticationItemsForAdapter.getItemsForAdapter()[position]
        makeItemsGone(holder.text, holder.number, holder.image)
        var f ={}
        when (obj) {
            is AuthenticationItemsForAdapter.ItemNumber -> {
                holder.number.visibility = View.VISIBLE
                holder.number.text = obj.number.toString()
                f = obj.f
            }
            is AuthenticationItemsForAdapter.ItemImage -> {
                holder.image.visibility = View.VISIBLE
                holder.image.setImageResource(obj.idImage)
                f = obj.f
            }
            is AuthenticationItemsForAdapter.ItemString -> {
                holder.text.visibility = View.VISIBLE
                holder.text.text = obj.str
                f = obj.f
            }
        }

        holder.itemView.setOnClickListener {
            f.invoke()
            onItemClickListener?.invoke()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return CARD_VIEW_TYPE
    }

    companion object {
        const val CARD_VIEW_TYPE = 1
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var number = itemView.findViewById<TextView>(R.id.numberTextView)
        var image = itemView.findViewById<ImageView>(R.id.itemImageView)
        var text = itemView.findViewById<TextView>(R.id.textTextView)
    }

    private fun makeItemsGone(firstTextView: TextView, secondTextView: TextView, image: ImageView) {
        firstTextView.visibility = View.GONE
        secondTextView.visibility = View.GONE
        image.visibility = View.GONE
    }
}
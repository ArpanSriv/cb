package com.arpan.collegebroker

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_flat_listing.view.*
import android.content.ClipData.Item



class FlatsAdapter(val context: Context?, val flats: ArrayList<Flat>): RecyclerView.Adapter<FlatsAdapter.FlatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlatViewHolder {
        return FlatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_flat_listing, parent, false))
    }

    override fun getItemCount() = flats.size

    override fun onBindViewHolder(holder: FlatViewHolder, position: Int) {
        holder.bindData(flats[position].name, flats[position].description, flats[position].price)
    }

    fun removeItem(position: Int) {
        flats.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun restoreItem(flat: Flat, position: Int) {
        flats.add(position, flat)
        // notify item added by position
        notifyItemInserted(position)
    }

    inner class FlatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val viewForeground: RelativeLayout = itemView.view_foreground

        override fun onClick(v: View?) {
            //TODO
        }

        fun bindData(name: String, description: String, price: Int) {
            itemView.name.text = name
            itemView.description.text = description
            itemView.price.text = price.toString()
        }

        init {
            itemView.setOnClickListener(this)
        }



    }

}
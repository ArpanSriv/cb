package com.arpan.collegebroker

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_flat_listing.view.*


class FlatsAdapter(val context: Context?, val flats: ArrayList<Flat>) : RecyclerView.Adapter<FlatsAdapter.FlatViewHolder>() {

    interface FavouriteListener {
        fun toggleFavourite(flatId: String, newState: Boolean)
    }

    var favouriteListener: FavouriteListener? = null
    var favouriteIdsList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlatViewHolder {
        return FlatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_flat_listing, parent, false))
    }

    override fun getItemCount() = flats.size

    override fun onBindViewHolder(holder: FlatViewHolder, position: Int) {
        try {
            holder.bindData(
                    flats[position].location,
                    flats[position].description,
                    flats[position].price.toInt(),
                    Uri.parse(flats[position].imagesLink.first()),
                    favouriteIdsList.contains(flats[position].flatId)
            )
        } catch (e: NoSuchElementException) {
            holder.bindData(
                    flats[position].location,
                    flats[position].description,
                    flats[position].price.toInt(),
                    null,
                    favouriteIdsList.contains(flats[position].flatId)
            )
        }
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

    inner class FlatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var id = ""
//        val checkListener: (CompoundButton, Boolean) -> Unit = { _, isChecked ->
//            if (isChecked) {
//                if (favouriteListener != null) {
//                    favouriteListener!!.toggleFavourite(flats[adapterPosition].flatId, isChecked)
//                }
//                itemView.favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.fav_filled))
//            } else {
//                if (favouriteListener != null) {
////                        favouriteListener!!.toggleFavourite(false)
//                }
//                itemView.favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.fav_black))
//            }
//        }

        override fun onClick(v: View?) {
            //TODO
        }

        @SuppressLint("SetTextI18n")
        fun bindData(name: String, description: String, price: Int, mainImageUri: Uri?, isFavourite: Boolean) {
            val formattedPrice = priceFormatter(price.toString())

            itemView.name.text = name
            itemView.description.text = description
            itemView.price.text = "₹ $formattedPrice"

            GlideApp
                    .with(context!!)
                    .load(mainImageUri)
                    .placeholder(R.drawable.home_red)
                    .into(itemView.thumbnail)
//
//
//            itemView.favToggleButton.setOnCheckedChangeListener(null)
//            itemView.favToggleButton.isChecked = isFavourite
//            itemView.favToggleButton.setOnCheckedChangeListener(checkListener)
        }

        init {
            itemView.setOnClickListener(this)
            itemView.favToggleButton.isChecked = false
            itemView.favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.fav_black))

//            itemView.favToggleButton.setOnCheckedChangeListener(checkListener)

        }


    }

}
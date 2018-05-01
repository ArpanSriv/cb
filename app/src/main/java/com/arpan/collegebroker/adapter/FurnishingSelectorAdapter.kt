package com.arpan.collegebroker.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.R
import kotlinx.android.synthetic.main.item_furnishing.view.*
import java.util.*

class FurnishingSelectorAdapter(val context: Context,
                                private val itemsHashMap: HashMap<String, Int>)
    : RecyclerView.Adapter<FurnishingSelectorAdapter.FurnishingViewHolder>() {

    private val furnishingCount = ArrayList<Int>(Collections.nCopies(itemsHashMap.size, 0))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnishingViewHolder {
        return FurnishingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_furnishing, parent, false))
    }

    override fun getItemCount() = itemsHashMap.size

    override fun onBindViewHolder(holder: FurnishingViewHolder, position: Int) {
        holder.bindData(itemsHashMap.keys.elementAt(position), itemsHashMap.values.elementAt(position))
    }

    inner class FurnishingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.counterPlusButton.setOnClickListener {
                furnishingCount[adapterPosition]++
                itemView.furnitureCountLabel.text = furnishingCount[adapterPosition].toString()
            }

            itemView.counterMinusButton.setOnClickListener {
                if (furnishingCount[adapterPosition] > 0) {
                    furnishingCount[adapterPosition]--
                    itemView.furnitureCountLabel.text = furnishingCount[adapterPosition].toString()
                }
            }
        }

        fun bindData(name: String, resourceId: Int) {
            itemView.furnitureNameLabel.text = name
            itemView.imageHolder_RV.setImageResource(resourceId)
        }
    }

}


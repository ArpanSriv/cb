package com.arpan.collegebroker

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.arpan.collegebroker.MyAppGlideModule
import kotlinx.android.synthetic.main.item_image.view.*
import android.os.Vibrator



class FlatImageAdapter(val context: Context, val uris: ArrayList<Uri>): RecyclerView.Adapter<FlatImageAdapter.FlatImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlatImageViewHolder {
        return FlatImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false))
    }

    override fun getItemCount(): Int {
        return uris.size
    }

    override fun onBindViewHolder(holder: FlatImageViewHolder, position: Int) {
        holder.bindImage(uris[position])
    }

    inner class FlatImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        init {
            itemView.setOnLongClickListener(this)
        }

        fun bindImage(uri: Uri) {
            GlideApp
                    .with(context)
                    .load(uri)
                    .fitCenter()
                    .into(itemView.flatImageView)
        }

        override fun onLongClick(v: View?): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
                        .vibrate(VibrationEffect.createOneShot(150, 80))
            }
            AlertDialog.Builder(context)
                    .setTitle("Are you sure?")
                    .setMessage("Remove this photo?")
                    .setPositiveButton("YES", { _: DialogInterface, _: Int ->
                        uris.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    })
                    .show()
            return true
        }
    }
}
package com.arpan.collegebroker.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.Contact
import com.arpan.collegebroker.GlideApp
import com.arpan.collegebroker.R
import com.arpan.collegebroker.ScrollingActivity
import kotlinx.android.synthetic.main.item_contact.view.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent



class ContactsAdapter(val context: Context, val contacts: ArrayList<Contact>): RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bindData(contacts[position].name, contacts[position].number, contacts[position].picUri)
    }


    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
            if (context is ScrollingActivity) {
                val uri = "tel:" + contacts[adapterPosition].number.trim()
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(uri)
                context.startActivity(intent)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        fun bindData(name: String, number: String, picUri: String?) {
            itemView.contactName_TextView.text = name
            itemView.contactNumber_TextView.text = number

            GlideApp
                    .with(context)
                    .load(picUri)
                    .placeholder(R.drawable.man)
                    .circleCrop()
                    .into(itemView.contact_imageView)
        }
    }
}
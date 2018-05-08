package com.arpan.collegebroker.fragment.allflats


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.Flat
import com.arpan.collegebroker.FlatsAdapter

import com.arpan.collegebroker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_my_flats.*

class MyFlatsFragment : IconFragment() {

    private lateinit var myFlatsAdapter: FlatsAdapter
    private val flats = ArrayList<Flat>()
    private val flatIds = ArrayList<String>()

    private val flatsReference = FirebaseDatabase.getInstance().reference.child("flats")
    private val currentUserReference = FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_flats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getFlatsOfUser()

        myFlatsAdapter = FlatsAdapter(context, flats)
        myFlatsRV.setEmptyView(myFlatsPlaceHolder)
        myFlatsRV.adapter = myFlatsAdapter
        myFlatsRV.layoutManager = LinearLayoutManager(context)
    }

    private fun getFlatsOfUser() {
        currentUserReference.child("listedFlats").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                val flatId = dataSnapshot!!.value!! as String

                flatsReference.child(flatId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        println("Cancelled")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        flats.add(dataSnapshot!!.getValue(Flat::class.java)!!)
                        myFlatsAdapter.notifyDataSetChanged()
                    }
                })
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun getIconDrawable(): Int {
        return R.drawable.ic_face_24dp
    }
}

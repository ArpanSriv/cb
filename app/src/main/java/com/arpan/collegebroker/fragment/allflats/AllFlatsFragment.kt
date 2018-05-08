package com.arpan.collegebroker.fragment.allflats


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arpan.collegebroker.Flat
import com.arpan.collegebroker.FlatsAdapter
import com.arpan.collegebroker.Prefs
import com.arpan.collegebroker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_all_flats.*


class AllFlatsFragment : IconFragment(), FlatsAdapter.FavouriteListener {

    private lateinit var allFlatsAdapter: FlatsAdapter
    private val flats = ArrayList<Flat>()

    private val flatsReference = FirebaseDatabase.getInstance().reference.child("flats")
    private val currentUserReference = FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid)

    private var userFavouriteFlats = ArrayList<String>()

    private lateinit var prefs: Prefs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        prefs = Prefs(context!!)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_flats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        flats.clear()
        allFlatsAdapter = FlatsAdapter(context, flats)
        allFlatsRV.setEmptyView(allFlatsPlaceHolder)
        allFlatsRV.adapter = allFlatsAdapter
        allFlatsAdapter.favouriteListener = this
        allFlatsAdapter.favouriteIdsList = userFavouriteFlats
        allFlatsRV.layoutManager = LinearLayoutManager(context)
//
//        userFavouriteFlats = prefs.prefs.getStringSet("favs", null)

//        updateUserFavs()
        updateFlats()
    }

    private fun updateFlats() {
        flatsReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(context, "Error: ${p0!!.code}", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                flats.add(dataSnapshot!!.getValue(Flat::class.java)!!)
                allFlatsAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun getIconDrawable(): Int {
        return R.drawable.numbered_list_24dp
    }

    fun updateUserFavs() {



//        currentUserReference.child("favouriteFlatIds").addChildEventListener(object : ChildEventListener {
//            override fun onCancelled(p0: DatabaseError?) {
//                Toast.makeText(context, "Error" + p0!!.message, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
//                userFavouriteFlats.add(dataSnapshot!!.value!! as String)
//                allFlatsAdapter.notifyDataSetChanged()
//            }
//
//            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
//                userFavouriteFlats.remove(dataSnapshot!!.value!! as String)
//
//                allFlatsAdapter.notifyDataSetChanged()
//            }
//        })
    }

    override fun toggleFavourite(flatId: String, newState: Boolean) {
        currentUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot!!.children.forEach {
                    if (it.key == "favouriteFlatIds") {
                        userFavouriteFlats.clear()
                        userFavouriteFlats.addAll(it.value as ArrayList<String>)

                        if (userFavouriteFlats.contains(flatId)) userFavouriteFlats.remove(flatId)
                        else userFavouriteFlats.add(flatId)

                        currentUserReference.updateChildren(mapOf("favouriteFlatIds" to userFavouriteFlats))

                        allFlatsAdapter.notifyDataSetChanged()
                        return
                    }
                }

                //favouriteFlatIds not found, create it and update the list online
                userFavouriteFlats.add(flatId)
                currentUserReference.updateChildren(mapOf("favouriteFlatIds" to userFavouriteFlats))
                allFlatsAdapter.notifyDataSetChanged()
            }
        })
    }
}

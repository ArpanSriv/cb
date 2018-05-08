package com.arpan.collegebroker.fragment.allflats


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.Flat
import com.arpan.collegebroker.FlatsAdapter

import com.arpan.collegebroker.R
import kotlinx.android.synthetic.main.fragment_favourite_flats.*

class FavouriteFlatsFragment : IconFragment() {

    private lateinit var favFlatsAdapter: FlatsAdapter
    private val flats = ArrayList<Flat>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_flats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favFlatsAdapter = FlatsAdapter(context, flats)
        favFlatsRV.setEmptyView(favFlatsPlaceHolder)
        favFlatsRV.adapter = favFlatsAdapter
    }

    override fun getIconDrawable(): Int {
        return R.drawable.ic_fav_24dp
    }
}

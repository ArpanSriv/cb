package com.arpan.collegebroker.fragment.createflat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.FurnishingItems

import com.arpan.collegebroker.R
import com.arpan.collegebroker.adapter.FurnishingSelectorAdapter
import kotlinx.android.synthetic.main.fragment_create_flat_furnishing.*

class CreateFlatFurnishingFragment : Fragment() {

    private lateinit var furnishingSelectorAdapter: FurnishingSelectorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_flat_furnishing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        furnishingSelectorAdapter = FurnishingSelectorAdapter(context!!, FurnishingItems)
        furnishingSelectorRecyclerView.layoutManager = LinearLayoutManager(context)
        furnishingSelectorRecyclerView.adapter = furnishingSelectorAdapter
    }
}

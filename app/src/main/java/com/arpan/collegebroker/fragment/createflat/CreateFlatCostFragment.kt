package com.arpan.collegebroker.fragment.createflat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.CreateFlatActivity

import com.arpan.collegebroker.R
import kotlinx.android.synthetic.main.fragment_create_flat_cost.*

class CreateFlatCostFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_flat_cost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ((activity) as CreateFlatActivity).submitCallbackListener = this
    }

    override fun submitFlatDetails(progress: Int) = priceEditText.text.toString()

    override fun reset() {
        priceEditText.setText("")
    }

    override fun getProgress() = 7

    override fun validateInputs() = priceEditText.text.toString() != ""

}

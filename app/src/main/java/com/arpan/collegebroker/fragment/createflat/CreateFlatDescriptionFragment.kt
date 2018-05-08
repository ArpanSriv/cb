package com.arpan.collegebroker.fragment.createflat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.CreateFlatActivity

import com.arpan.collegebroker.R
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.fragment_create_flat_description.*

class CreateFlatDescriptionFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_flat_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ((activity) as CreateFlatActivity).submitCallbackListener = this
    }

    override fun submitFlatDetails(progress: Int): Any {
        return descriptionBox.text.toString()
    }

    override fun getProgress() = 6

    override fun reset() {
        descriptionBox.setText("")
    }

    override fun validateInputs() = descriptionBox.text.toString() != ""
}

package com.arpan.collegebroker.fragment.createflat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.arpan.collegebroker.CreateFlatActivity
import com.arpan.collegebroker.R
import kotlinx.android.synthetic.main.fragment_create_flat_bhk.*

class CreateFlatBhkFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    private val bhkOnClickListener = View.OnClickListener {
        toggleBhkSelectionState(it as ImageButton)
    }

    private val bhkSelection = booleanArrayOf(false, false, false, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_create_flat_bhk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onebhkButton.setOnClickListener(bhkOnClickListener)
        twobhkButton.setOnClickListener(bhkOnClickListener)
        threebhkButton.setOnClickListener(bhkOnClickListener)
        fourbhkButton.setOnClickListener(bhkOnClickListener)
    }

    private fun toggleBhkSelectionState(imageButton: ImageButton) {
        if (imageButton == onebhkButton) {
            initBhkSelectionArray()
            bhkSelection[0] = true
        }

        else if (imageButton == twobhkButton) {
            initBhkSelectionArray()
            bhkSelection[1] = true
        }

        else if (imageButton == threebhkButton) {
            initBhkSelectionArray()
            bhkSelection[2] = true
        }

        else if (imageButton == fourbhkButton) {
            initBhkSelectionArray()
            bhkSelection[3] = true
        }
    }

    private fun initBhkSelectionArray() {
        for (i in 0 until bhkSelection.size) {
            bhkSelection[i] = false
        }
    }

    override fun submitFlatDetails(progress: Int): Any {
        for (i in 0 until bhkSelection.size) {
            if (bhkSelection[i]) return i
        }
        return -1
    }

    override fun getProgress() = 2
}

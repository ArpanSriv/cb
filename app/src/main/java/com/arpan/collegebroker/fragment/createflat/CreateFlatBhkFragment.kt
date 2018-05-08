package com.arpan.collegebroker.fragment.createflat

import android.media.Image
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.arpan.collegebroker.CreateFlatActivity
import com.arpan.collegebroker.R
import com.arpan.collegebroker.switchImageToggleState
import kotlinx.android.synthetic.main.fragment_create_flat_bhk.*

class CreateFlatBhkFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    private val bhkOnClickListener = View.OnClickListener {
        initialStateChanged = true
        toggleBhkSelectionState(it as ImageButton)
    }

    private val bhkSelection = booleanArrayOf(false, false, false, false)
    private lateinit var bhkImages: Array<ImageView>
    private var initialStateChanged = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_create_flat_bhk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ((activity) as CreateFlatActivity).submitCallbackListener = this

        bhkImages = arrayOf(onebhkButton, twobhkButton, threebhkButton, fourbhkButton)

        onebhkButton.setOnClickListener(bhkOnClickListener)
        twobhkButton.setOnClickListener(bhkOnClickListener)
        threebhkButton.setOnClickListener(bhkOnClickListener)
        fourbhkButton.setOnClickListener(bhkOnClickListener)
    }

    private fun toggleBhkSelectionState(imageButton: ImageButton) {
        if (imageButton == onebhkButton) {
            resetBhkSelectionArray()
            bhkSelection[0] = true
            updateSelectionState()
        }

        else if (imageButton == twobhkButton) {
            resetBhkSelectionArray()
            bhkSelection[1] = true
            updateSelectionState()
        }

        else if (imageButton == threebhkButton) {
            resetBhkSelectionArray()
            bhkSelection[2] = true
            updateSelectionState()
        }

        else if (imageButton == fourbhkButton) {
            resetBhkSelectionArray()
            bhkSelection[3] = true
            updateSelectionState()
        }
    }

    private fun updateSelectionState() {
        for (i in 0 until 4) {
            switchImageToggleState(bhkImages[i], bhkSelection[i])
        }
    }

    private fun resetBhkSelectionArray() {
        for (i in 0 until 4) {
            bhkSelection[i] = false
            switchImageToggleState(bhkImages[i], bhkSelection[i])
        }
    }

    override fun submitFlatDetails(progress: Int): Any {
        for (i in 0 until bhkSelection.size) {
            if (bhkSelection[i]) return i
        }
        return -1
    }

    override fun getProgress() = 2

    override fun reset() {
        resetBhkSelectionArray()
    }

    override fun validateInputs() = initialStateChanged
}

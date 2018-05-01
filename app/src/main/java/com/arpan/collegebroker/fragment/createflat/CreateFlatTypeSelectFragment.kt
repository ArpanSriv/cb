package com.arpan.collegebroker.fragment.createflat

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.arpan.collegebroker.CreateFlatActivity
import com.arpan.collegebroker.Flat
import com.arpan.collegebroker.R
import com.arpan.collegebroker.switchImageToggleState
import kotlinx.android.synthetic.main.fragment_create_flat_type_select.*
import kotlinx.android.synthetic.main.item_image.*


class CreateFlatTypeSelectFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    var type = 1 //1.Flat 2.Villa
    var villaState = true
    var flatState = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_create_flat_type_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as CreateFlatActivity).submitCallbackListener = this

        switchImageToggleState(villaImage, villaState)
        switchImageToggleState(flatImage, flatState)

        villaImage.setOnClickListener {
            switchImageToggleState(it as ImageView, villaState)
            villaState = !villaState

            switchImageToggleState(flatImage, flatState)
            flatState = !flatState

        }

        flatImage.setOnClickListener {
            switchImageToggleState(it as ImageView, flatState)
            flatState = !flatState

            switchImageToggleState(villaImage, villaState)
            villaState = !villaState

            Toast.makeText(context, "VillaState: " + villaState + " FlatState: " + flatState, Toast.LENGTH_SHORT).show()
        }
    }

    override fun submitFlatDetails(progress: Int): Any {
        if (villaState) type = 2
        else            type = 1
        return type
    }

    override fun getProgress() = 1


}

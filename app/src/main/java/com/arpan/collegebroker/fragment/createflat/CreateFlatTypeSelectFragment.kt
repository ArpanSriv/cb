package com.arpan.collegebroker.fragment.createflat

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.arpan.collegebroker.*
import kotlinx.android.synthetic.main.fragment_create_flat_type_select.*
import kotlinx.android.synthetic.main.item_image.*
import com.arpan.collegebroker.AnimationUtils.registerCircularRevealAnimation


class CreateFlatTypeSelectFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    var type = 1 //1.Flat 2.Villa
    var villaState = true
    var flatState = false

    var initialStateChanged = false

    private val ARG_REVEAL_SETTINGS: String? = "ARG"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return LayoutInflater.from(context).inflate(R.layout.fragment_create_flat_type_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        AnimationUtils.registerCircularRevealAnimation(context, typeSelectorHolder, arguments!!.getParcelable(ARG_REVEAL_SETTINGS), context!!.getColor(R.color.carbon_red_a400), context!!.getColor(R.color.white))

        (activity as CreateFlatActivity).submitCallbackListener = this

        switchImageToggleState(villaImage, true)
        switchImageToggleState(flatImage, true)

        villaImage.setOnClickListener {
            initialStateChanged = true
            switchImageToggleState(it as ImageView, villaState)
            villaState = !villaState

            switchImageToggleState(flatImage, flatState)
            flatState = !flatState

        }

        flatImage.setOnClickListener {
            initialStateChanged = true
            switchImageToggleState(it as ImageView, flatState)
            flatState = !flatState

            switchImageToggleState(villaImage, villaState)
            villaState = !villaState

            Toast.makeText(context, "VillaState: $villaState FlatState: $flatState", Toast.LENGTH_SHORT).show()
        }
    }

    override fun submitFlatDetails(progress: Int): Any {
        return if (villaState) 1; else 2
    }

    override fun getProgress() = 1

    override fun reset() {
        switchImageToggleState(villaImage, false)
        switchImageToggleState(flatImage, false)
    }

    override fun validateInputs() = initialStateChanged

    companion object {
        @JvmStatic
        fun newInstance(ras: RevealAnimationSetting): CreateFlatTypeSelectFragment {
            return CreateFlatTypeSelectFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_REVEAL_SETTINGS, ras)
                }
            }
        }
    }
}

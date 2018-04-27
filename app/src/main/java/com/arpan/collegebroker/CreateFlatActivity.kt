package com.arpan.collegebroker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_container.*

class CreateFlatActivity : AppCompatActivity() {

    interface SubmitCallbackListener {
        fun submitFlatDetails()
    }

    var submitCallbackListener: SubmitCallbackListener? = null

    private var fragments: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

//        initFragments()
        println("Lol")
    }

//    private fun initFabs() {
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//
//            override fun onPageScrolled(current: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                if (current == 0) { //SellingFragment
//                    setHorizontalBias(uploadContactFab.id, 0.5f + positionOffset / 4)
//                    setHorizontalBias(uploadPhotoFab.id, 0.5f - positionOffset / 4)
//
//                    mainFab.setOnClickListener { viewPager.setCurrentItem(1, true) }
//                }
//
//                else if (current == 1) {
//                    mainFab.setOnClickListener { submitCallbackListener!!.submitFlatDetails() }
//                }
//            }
//
//            override fun onPageSelected(position: Int) {
//                if (position == 0) {
//                    uploadPhotoFab.hide()
//                    uploadContactFab.hide()
//                }
//
//                if (position == 1) {
//                    uploadPhotoFab.show()
//                    uploadContactFab.show()
//                }
//
//            }
//
//        })
//    }

    private fun setHorizontalBias(layout: Int, bias: Float) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(activity_container)

        constraintSet.apply {
            setHorizontalBias(layout, bias)
        }

        constraintSet.applyTo(activity_container)
    }
//
//    private fun initFragments() {
//
//        fragments.add(SellingFragment())
//        fragments.add(CreateFlatFragment())
//    }
}

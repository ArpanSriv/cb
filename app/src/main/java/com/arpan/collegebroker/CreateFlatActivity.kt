package com.arpan.collegebroker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.arpan.collegebroker.fragment.createflat.CreateFlatBhkFragment
import com.arpan.collegebroker.fragment.createflat.CreateFlatFurnishingFragment
import com.arpan.collegebroker.fragment.createflat.CreateFlatTypeSelectFragment
import kotlinx.android.synthetic.main.activity_container.*

class CreateFlatActivity : AppCompatActivity() {

    interface SubmitCallbackListener {
        fun submitFlatDetails()
    }

    var submitCallbackListener: SubmitCallbackListener? = null

    private var fragments: ArrayList<Fragment> = ArrayList()
    private var currentFragmentIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        initFragments()
        initFabListener()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(fragmentsHolder.id, CreateFlatTypeSelectFragment())
        fragmentTransaction.commit()
    }

    private fun initFragments() {
        fragments.add(CreateFlatTypeSelectFragment())
        fragments.add(CreateFlatBhkFragment())
        fragments.add(CreateFlatFurnishingFragment())
    }

    private fun initFabListener() {
        mainFab.setOnClickListener {
            //Validate Data
            updateFragment(currentFragmentIndex)
        }
    }

    private fun updateFragment(index: Int) {
        val newIndex = (index + 1)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentsHolder.id, fragments[newIndex])
        fragmentTransaction.commit()
    }
}

package com.arpan.collegebroker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.arpan.collegebroker.fragment.createflat.CreateFlatBhkFragment
import com.arpan.collegebroker.fragment.createflat.CreateFlatFurnishingFragment
import com.arpan.collegebroker.fragment.createflat.CreateFlatLocation
import com.arpan.collegebroker.fragment.createflat.CreateFlatTypeSelectFragment
import kotlinx.android.synthetic.main.activity_container.*

class CreateFlatActivity : AppCompatActivity() {

    interface SubmitCallbackListener {
//        fun isReady(): Boolean
        fun submitFlatDetails(progress: Int): Any
        fun getProgress(): Int
    }

    private val currentFlat = Flat()

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
        fragments.add(CreateFlatLocation())
        fragments.add(CreateFlatFurnishingFragment())
    }

    private fun initFabListener() {
        mainFab.setOnClickListener {
            //Validate Data
            updateFragment(currentFragmentIndex)
        }
    }

    private fun updateFragment(index: Int) {
        setFlatProgress()

        val newIndex = (index + 1)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentsHolder.id, fragments[newIndex])
        fragmentTransaction.commit()

        if (currentFragmentIndex == fragments.size - 1) currentFragmentIndex = 0
        else currentFragmentIndex++
    }

    private fun setFlatProgress() {
        when (submitCallbackListener!!.getProgress()) {
            1 -> {
                currentFlat.type = submitCallbackListener!!.submitFlatDetails(1) as Int
                Log.d(CreateFlatActivity::class.java.simpleName, "Set type ${currentFlat.type}")
            }

            2 -> {
                currentFlat.bhk = submitCallbackListener!!.submitFlatDetails(2) as Int
                Log.d(CreateFlatActivity::class.java.simpleName, "Set bhk ${currentFlat.bhk}")
            }

            3 -> {
                if (submitCallbackListener!!.submitFlatDetails(3) != false) {
                    currentFlat.name = submitCallbackListener!!.submitFlatDetails(3) as String
                    Log.d(CreateFlatActivity::class.java.simpleName, "Set name ${currentFlat.name}")
                }
            }

        }
    }
}

package com.arpan.collegebroker

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(val fragments: ArrayList<Fragment>, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {



    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "All Flats"
            1 -> "Add Flat"
            else -> "Lol"
        }
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}
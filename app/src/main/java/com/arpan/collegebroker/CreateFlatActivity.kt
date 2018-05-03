package com.arpan.collegebroker

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toolbar
import com.arpan.collegebroker.fragment.createflat.*
import kotlinx.android.synthetic.main.activity_container.*
import android.databinding.adapters.CompoundButtonBindingAdapter.setChecked
import android.support.design.widget.NavigationView
import android.view.MenuItem
import android.support.v4.view.GravityCompat
import com.arpan.collegebroker.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class CreateFlatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    interface SubmitCallbackListener {
        //        fun isReady(): Boolean
        fun submitFlatDetails(progress: Int): Any

        fun getProgress(): Int
    }

    private val currentFlat = Flat()

    var submitCallbackListener: SubmitCallbackListener? = null

    private var fragments: ArrayList<Fragment> = ArrayList()
    private var currentFragmentIndex = -1

    private lateinit var mToolbar: android.support.v7.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        mToolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        toolbar.logoDescription = resources.getString(R.string.logo_desc)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        initFragments()
        initFabListener()
        initNavigationView()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
        fragmentTransaction.add(fragmentsHolder.id, CreateFlatWelcomeFragment())
        fragmentTransaction.commit()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun initNavigationView() {
        nav_view.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            // close drawer when item is tapped
            activity_container_root.closeDrawers()

            true
        }

    }

    private fun initFragments() {

        fragments.add(CreateFlatTypeSelectFragment())
        fragments.add(CreateFlatBhkFragment())
        fragments.add(CreateFlatLocationFragment())
        fragments.add(CreateFlatFurnishingFragment())
        fragments.add(CreateFlatPhotoGalleryFragment())
    }

    private fun initFabListener() {
        mainFab.setOnClickListener {
            //Validate Data
            updateFragment(currentFragmentIndex)
        }
    }

    private fun updateFragment(index: Int) {
        if (index != -1) setFlatProgress() //Exclude being called when first replace

        val newIndex = (index + 1)

        if (newIndex == 4) mainFab.hide()
        else mainFab.show()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
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

            4 -> {
                val countArray = submitCallbackListener!!.submitFlatDetails(4) as ArrayList<Int>
                currentFlat.furnishing = Furnishing(
                        sofas = countArray[0],
                        tv = countArray[1],
                        fridge = countArray[3],
                        washingMachine = countArray[4],
                        tableCount = countArray[5],
                        chairCount = countArray[6],
                        singleBedCount = countArray[7],
                        doubleBedCount = countArray[8]
                )
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity_container_root.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this@CreateFlatActivity, LoginActivity::class.java))
        }
        return true
    }
}

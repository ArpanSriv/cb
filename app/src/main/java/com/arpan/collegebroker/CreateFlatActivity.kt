package com.arpan.collegebroker

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.arpan.collegebroker.fragment.createflat.*
import kotlinx.android.synthetic.main.activity_container.*
import android.net.Uri
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import com.arpan.collegebroker.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.kofigyan.stateprogressbar.StateProgressBar
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.header_navigation_drawer.*


class CreateFlatActivity : AppCompatActivity() {

    interface SubmitCallbackListener {
        //        fun isReady(): Boolean
        fun submitFlatDetails(progress: Int): Any
        fun validateInputs(): Boolean
        fun reset()
        fun getProgress(): Int
    }

    private val currentFlat = Flat()

    var submitCallbackListener: SubmitCallbackListener? = null

    private var fragments: ArrayList<Fragment> = ArrayList()
    private var currentFragmentIndex = -1

    private lateinit var mToolbar: android.support.v7.widget.Toolbar

    private val mCurrentUser = FirebaseAuth.getInstance().currentUser

    private val bottomNavigationDescriptions = arrayOf("Type", "Size", "Location", "Details", "Description")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        mToolbar = findViewById(R.id.toolbar)

        window.statusBarColor = ContextCompat.getColor(this, R.color.carbon_red_a400)

        setSupportActionBar(toolbar)
        toolbar.logoDescription = resources.getString(R.string.logo_desc)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        stateProgressBar.setStateDescriptionData(bottomNavigationDescriptions)

        initFragments()
        initFabListener()
        initNavigationView()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
        fragmentTransaction.add(fragmentsHolder.id, CreateFlatWelcomeFragment())
        fragmentTransaction.commit()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        val emailTV = header.findViewById<TextView>(R.id.nameNavigationHeader);
        emailTV.text = mCurrentUser!!.email
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun initNavigationView() {
        nav_view.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.nav_list_a_flat -> {
                    AlertDialog.Builder(this)
                            .setMessage("Are you sure you want to lose your current progress and start again?")
                            .setTitle("List a flat")
                            .setPositiveButton("YES", { _, _ ->
                                run {
                                    startActivity(Intent(this@CreateFlatActivity, CreateFlatActivity::class.java))
                                    finish()
                                }
                            })
                            .setNegativeButton("NO", { _, _ -> })
                            .show()
                }

                R.id.nav_rent_a_flat -> {
                    AlertDialog.Builder(this)
                            .setMessage("Are you sure you want to lose your current progress and look for flats?")
                            .setTitle("Rent a flat")
                            .setPositiveButton("YES", { _, _ ->
                                run {
                                    startActivity(Intent(this@CreateFlatActivity, AllFlatsActivity::class.java))
                                    finish()
                                }
                            })
                            .setNegativeButton("NO", { _, _ -> })
                            .show()
                }

                R.id.nav_logout -> {
                    AlertDialog.Builder(this)
                            .setMessage("Are you sure you want to logout?")
                            .setTitle("Rent a flat")
                            .setPositiveButton("YES", { _, _ ->
                                run {
                                    FirebaseAuth.getInstance().signOut()
                                    startActivity(Intent(this@CreateFlatActivity, LoginActivity::class.java))
                                    finish()
                                }
                            })
                            .setNegativeButton("NO", { _, _ -> })
                            .show()
                }
            }

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
        fragments.add(CreateFlatDescriptionFragment())
        fragments.add(CreateFlatCostFragment())
    }

    private fun initFabListener() {
        mainFab.setOnClickListener {
            //Validate Data
            updateFragment(currentFragmentIndex)
        }
    }

    private fun updateFragment(index: Int) {
        if (!submitCallbackListener!!.validateInputs()) {
            AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please ensure you have entered all the required fields.")
                    .setPositiveButton("OK", { _, _ -> })
                    .show()
            return
        } else {

            val newIndex = (index + 1)

            if (index == -1) {
                supportFragmentManager
                        .beginTransaction()
                        .replace(fragmentsHolder.id, CreateFlatTypeSelectFragment.newInstance(constructRevealSettings()), CreateFlatTypeSelectFragment::class.java.simpleName)
                        .commit()
                currentFragmentIndex++
                stateProgressBarHolder.visibility = View.VISIBLE
                return
            } else {
                if (newIndex == fragments.size) {
                    setFlatProgress()
                    mainFab.hide()
                    stateProgressBarHolder.visibility = View.GONE
                    supportFragmentManager
                            .beginTransaction()
                            .replace(fragmentsHolder.id, EndFragment.newInstance(currentFlat), EndFragment::class.java.simpleName)
                            .commit()
                    return
                } else {
                    setFlatProgress()
                    when (newIndex) {
                        0 -> stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                        1 -> stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                        2 -> stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                        3 -> stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                        4 -> stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE)
                    }
                }
            } //Exclude being called when first replace


            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            fragmentTransaction.replace(fragmentsHolder.id, fragments[newIndex])
            fragmentTransaction.commit()

            if (currentFragmentIndex == fragments.size - 1) currentFragmentIndex = 0
            else currentFragmentIndex++
        }
    }

    private fun setFlatProgress() {
        when (submitCallbackListener!!.getProgress()) {
            1 -> {
                currentFlat.type = submitCallbackListener!!.submitFlatDetails(1) as Int
            }

            2 -> {
                currentFlat.bhk = (submitCallbackListener!!.submitFlatDetails(2) as Int) + 1
            }

            3 -> {
                if (submitCallbackListener!!.submitFlatDetails(3) != false) {
                    currentFlat.location = submitCallbackListener!!.submitFlatDetails(3) as String
                    Log.d(CreateFlatActivity::class.java.simpleName, "Set location ${currentFlat.location}")
                }
            }

            4 -> {
                val countArray = submitCallbackListener!!.submitFlatDetails(4) as ArrayList<Int>
                currentFlat.furnishing = Furnishing(
                        sofas = countArray[0],
                        tv = countArray[1],
                        fridge = countArray[2],
                        washingMachine = countArray[3],
                        tableCount = countArray[4],
                        chairCount = countArray[5],
                        singleBedCount = countArray[6],
                        doubleBedCount = countArray[7]
                )
            }

            5 -> {
                val pair = submitCallbackListener!!.submitFlatDetails(5) as kotlin.Pair<ArrayList<Uri>, ArrayList<Contact>>
                val images = pair.first
                val contacts = pair.second
                val imagesUriInString = ArrayList<String>()
                images.forEach {
                    imagesUriInString.add(it.toString())
                }
                currentFlat.images = imagesUriInString
                currentFlat.contacts = contacts
            }

            6 -> {
                currentFlat.description = submitCallbackListener!!.submitFlatDetails(6) as String
            }

            7 -> {
                currentFlat.price = submitCallbackListener!!.submitFlatDetails(7) as String
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            activity_container_root.openDrawer(GravityCompat.START)
            true
        }

        R.id.resetButton -> {
            val alertDialog = AlertDialog.Builder(this)
                    .setTitle("RESET")
                    .setMessage("Are you sure you want to clear all the data? This change isn't reversible.")
                    .setPositiveButton("Yes", { _: DialogInterface, _: Int ->
                        submitCallbackListener!!.reset()

                    })
                    .setNegativeButton("No", { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    })
                    .show()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun constructRevealSettings(): RevealAnimationSetting {
        return RevealAnimationSetting.create(
                (mainFab.x + mainFab.width / 2).toInt(),
                (mainFab.y + mainFab.height / 2).toInt(),
                activity_container_root.width,
                activity_container_root.height)
    }
}

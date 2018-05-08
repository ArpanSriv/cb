package com.arpan.collegebroker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.arpan.collegebroker.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var prefs: Prefs? = null
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1234

    private var mLocationPermissionsGranted: Boolean = false
    private var mAccessPermissionGranted = false

    private val mAuth = FirebaseAuth.getInstance()
    private val mUsersFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java)!!
                if (connected) {
                    rentLabel.visibility = View.VISIBLE
                    buyButton.visibility = View.VISIBLE
                    sellButton.visibility = View.VISIBLE
                    listLabel.visibility = View.VISIBLE
                    mainActivityProgressBar.visibility = View.GONE
                    val snackbar = Snackbar.make(mainActivityRoot, "Connection Successful", Snackbar.LENGTH_SHORT)
                    snackbar.view.setBackgroundColor(Color.parseColor("#388E3C"))
                    snackbar.setActionTextColor(Color.WHITE)
                    snackbar.show()
                } else {
                    rentLabel.visibility = View.INVISIBLE
                    buyButton.visibility = View.INVISIBLE
                    sellButton.visibility = View.INVISIBLE
                    listLabel.visibility = View.INVISIBLE
                    mainActivityProgressBar.visibility = View.VISIBLE
                    val snackbar = Snackbar.make(mainActivityRoot, "Connecting...", Snackbar.LENGTH_INDEFINITE)
                    snackbar.view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.error_color_material))
                    snackbar.show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                System.err.println("Listener was cancelled")
            }
        })

        do {
            getPermissions()
        } while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

        prefs = Prefs(this)

        sellButton.setOnClickListener {
            prefs!!.category = 1 //Seller
            val intent = Intent(this@MainActivity, CreateFlatActivity::class.java)
            startActivity(intent)
        }

        buyButton.setOnClickListener {
            prefs!!.category = 2 //Buyer
            val intent = Intent(this@MainActivity, AllFlatsActivity::class.java)
            startActivity(intent)
        }

        if (mAuth.currentUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        } else {
            val currentFirebaseUser = mAuth.currentUser
            val user = User(
                    email = currentFirebaseUser!!.email!!,
                    uid = currentFirebaseUser.uid
            )


            mUsersFirebaseDatabaseReference.child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    if (!dataSnapshot!!.exists()) {
                        mUsersFirebaseDatabaseReference.child(user.uid).setValue(user)
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    Log.d("MainActivity", p0!!.message)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getPermissions() {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true

            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE)
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE)
        }

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mAccessPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    111)
        }
    }

}

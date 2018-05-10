package com.arpan.collegebroker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.arpan.collegebroker.adapter.ImageSliderAdapter
import kotlinx.android.synthetic.main.activity_flat_details.*
import java.util.*

class FlatDetailsActivity : AppCompatActivity() {

    private lateinit var flat: Flat
    private lateinit var imageSliderAdapter: ImageSliderAdapter

    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flat_details)

        flat = intent.getSerializableExtra("FLAT") as Flat

        initImageSlider()
    }

    private fun initImageSlider() {
        imageSliderAdapter = ImageSliderAdapter(this, flat.imagesLink)

        imagePagerIndicator.setViewPager(imagePager)

        val handler = Handler()
        val Update = Runnable {
            if (currentPage == flat.imagesLink.size) {
                currentPage = 0
            }
            imagePager.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 2500, 2500)
    }
}

package com.arpan.collegebroker.fragment.createflat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.CreateFlatActivity
import com.arpan.collegebroker.PlaceAutoCompleteAdapter
import com.arpan.collegebroker.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.fragment_create_flat_location.*

class CreateFlatLocation : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    var mPlaceAutoCompleteAdapter: PlaceAutoCompleteAdapter? = null
    var mGoogleApiClient: GoogleApiClient? = null
    private val LAT_LNG_BOUNDS = LatLngBounds(
            LatLng(-40.0, -168.0), LatLng(71.0, 136.0))

    override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_create_flat_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPlaceAutoCompleteAdapter = PlaceAutoCompleteAdapter(context, mGoogleApiClient, LAT_LNG_BOUNDS, null)
        locationAutocomplete.setAdapter(mPlaceAutoCompleteAdapter)
    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient!!.disconnect()
    }

    override fun submitFlatDetails(progress: Int): Any {
        if (locationAutocomplete.text.toString() == "") {
            return locationAutocomplete.text.toString()
        }
        return false
    }

    override fun getProgress() = 3
}

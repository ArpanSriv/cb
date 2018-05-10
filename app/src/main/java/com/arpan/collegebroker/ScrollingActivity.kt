package com.arpan.collegebroker

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.Toast
import com.arpan.collegebroker.adapter.ContactsAdapter
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_scrolling.*
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class ScrollingActivity : AppCompatActivity(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private lateinit var flat: Flat

    private val MAP_VIEW_BUNDLE_KEY: String = "BUNDLE_KEY"

    private val bhkImages = arrayOf(R.drawable.onebhk, R.drawable.twobhk, R.drawable.threebhk, R.drawable.fourbhk)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        locationMapView.onCreate(mapViewBundle)

        locationMapView.getMapAsync(this)

        UpdateUITask(intent, savedInstanceState).execute()
    }

    inner class UpdateUITask(val intent: Intent, val bundle: Bundle?) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            flat = intent.getSerializableExtra("FLAT") as Flat
//            initMapView(bundle)
            return null
        }

        override fun onPostExecute(result: Void?) {
            updateFlatValues(flat)
        }

    }

    private fun updateFlatValues(flat: Flat) {
        bhkImage_Card.setImageResource(bhkImages[flat.bhk])
        bhkIndicatorLabel.text = generateFlatTypeDetails(flat)

        priceValueTV.text = "₹ ${priceFormatter(flat.price)}"

        updateFurnitureValues(flat.furnishing)

        descriptionTextView.text = flat.description

        locationTV_Card.text = flat.location.split(",")[0]

        attachedContactsRecyclerView.adapter = ContactsAdapter(this, flat.contacts)
        attachedContactsRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun updateFurnitureValues(furnishing: Furnishing?) {
        sofaCount.text = furnishing!!.sofas.toString()
        tvCount.text = furnishing.tv.toString()
        fridgeCount.text = furnishing.fridge.toString()
        wmCount.text = furnishing.washingMachine.toString()
        chairCount.text = furnishing.chairCount.toString()
        tableCount.text = furnishing.tableCount.toString()
        sBedCount.text = furnishing.singleBedCount.toString()
        dBedCount.text = furnishing.doubleBedCount.toString()

        if (furnishing.sofas == 0) switchImageToggleState(sofaItemIV, false)
        if (furnishing.tv == 0) switchImageToggleState(tvItemIV, false)
        if (furnishing.fridge == 0) switchImageToggleState(fridgeItemIV, false)
        if (furnishing.washingMachine == 0) switchImageToggleState(wmItemIV, false)
        if (furnishing.chairCount == 0) switchImageToggleState(chairItemIV, false)
        if (furnishing.tableCount == 0) switchImageToggleState(tableItemIV, false)
        if (furnishing.singleBedCount == 0) switchImageToggleState(singleBedItemIV, false)
        if (furnishing.doubleBedCount == 0) switchImageToggleState(doubleBedItemIV, false)

    }

    private fun generateFlatTypeDetails(flat: Flat): String {
        val type: String
        val bhk: String

        when (flat.type) {
            1 -> type = "Flat"
            2 -> type = "Villa"
            else -> type = "Flat"
        }

        when (flat.bhk) {
            0 -> bhk = "1 BHK"
            1 -> bhk = "2 BHK"
            2 -> bhk = "3 BHK"
            3 -> bhk = "4+ BHK"
            else -> bhk = "1 BHK"
        }

        return "$bhk $type"
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        locationMapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        locationMapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        locationMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        locationMapView.onStop()
    }

    override fun onPause() {
        locationMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        locationMapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        locationMapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map

        val coordinates = getLatLongFromPlace(flat.location)

        updateMarkerWithCircle(googleMap!!, coordinates, flat.location.split(",")[0], 15F)
    }

    private fun getLatLongFromPlace(place: String): LatLng {
        try {
            val selected_place_geocoder = Geocoder(this)
            val address: List<Address>?

            address = selected_place_geocoder.getFromLocationName(place, 5)

            if (address == null) {
                return LatLng(0.toDouble(), 99.toDouble())
            } else {
                val location = address[0]
                val lat = location.latitude
                val lng = location.longitude
                return LatLng(lat, lng)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            val fetch_latlng_from_service_abc = fetchLatLongFromService(
                    place.replace("\\s+".toRegex(), ""))
            fetch_latlng_from_service_abc.execute()

        }
        return LatLng(0.toDouble(), 99.toDouble())
    }

    private fun updateMarkerWithCircle(googleMap: GoogleMap, position: LatLng, text: String, zoomLevel: Float) {
        googleMap.clear()

        googleMap
                .addMarker(MarkerOptions()
                        .position(position)
                        .title(text)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .draggable(true))

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))
    }

    @SuppressLint("StaticFieldLeak")
    inner class fetchLatLongFromService(var place: String) : AsyncTask<Void, Void, StringBuilder>() {

        override fun onCancelled() {
            super.onCancelled()
            this.cancel(true)
        }

        override fun doInBackground(vararg params: Void): StringBuilder? {
            try {
                var conn: HttpURLConnection? = null
                val jsonResults = StringBuilder()
                val googleMapUrl = ("http://maps.googleapis.com/maps/api/geocode/json?address="
                        + this.place + "&sensor=false")

                val url = URL(googleMapUrl)
                conn = url.openConnection() as HttpURLConnection
                val `in` = InputStreamReader(
                        conn.getInputStream())
                var read: Int
                val buff = CharArray(1024)
                while ((`in`.read(buff)) != -1) {
                    read = `in`.read(buff)
                    jsonResults.append(buff, 0, read)
                }
                val a = ""
                return jsonResults
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null

        }

        override fun onPostExecute(result: StringBuilder) {
            // TODO Auto-generated method stub
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result.toString())
                val resultJsonArray = jsonObj.getJSONArray("results")

                // Extract the Place descriptions from the results
                // resultList = new ArrayList<String>(resultJsonArray.length());

                val before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0)

                val geometry_jsonObj = before_geometry_jsonObj
                        .getJSONObject("geometry")

                val location_jsonObj = geometry_jsonObj
                        .getJSONObject("location")

                val lat_helper = location_jsonObj.getString("lat")
                val lat = java.lang.Double.valueOf(lat_helper)!!


                val lng_helper = location_jsonObj.getString("lng")
                val lng = java.lang.Double.valueOf(lng_helper)!!


                val point = LatLng(lat, lng)


            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()

            }

        }
    }


}

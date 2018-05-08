package com.arpan.collegebroker.fragment.createflat

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.arpan.collegebroker.CreateFlatActivity
import com.arpan.collegebroker.PlaceAutoCompleteAdapter
import com.arpan.collegebroker.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.fragment_create_flat_location.*
import com.google.android.gms.maps.model.MarkerOptions
import android.os.AsyncTask.execute
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray
import android.R.string.cancel
import android.annotation.SuppressLint
import android.graphics.Camera
import android.os.AsyncTask
import android.widget.RadioGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MapStyleOptions
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class CreateFlatLocationFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener, OnMapReadyCallback {

    var mPlaceAutoCompleteAdapter: PlaceAutoCompleteAdapter? = null
    var mGoogleApiClient: GoogleApiClient? = null
    private val LAT_LNG_BOUNDS = LatLngBounds(
            LatLng(-40.0, -168.0), LatLng(71.0, 136.0))

    private var currentSelectionAddress: String = "Pashan, Pune, Maharashtra, India"
    private var map: GoogleMap? = null

    var mapFragment: Fragment? = null

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
        ((activity) as CreateFlatActivity).submitCallbackListener = this
        mPlaceAutoCompleteAdapter = PlaceAutoCompleteAdapter(context, mGoogleApiClient, LAT_LNG_BOUNDS, null)
        locationAutocomplete.setAdapter(mPlaceAutoCompleteAdapter)

        locationAutocomplete.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            currentSelectionAddress = mPlaceAutoCompleteAdapter!!.getItem(position).toString()

            map!!.clear()

            val latLng = getLatLongFromPlace(currentSelectionAddress)
            val marker = MarkerOptions().position(latLng)
            map!!.addMarker(marker)

            val location: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15F)
            map!!.animateCamera(location)
        }

        mapFragment = fragmentManager!!.findFragmentById(R.id.map) as SupportMapFragment
        (mapFragment as SupportMapFragment).getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_dark_style))
    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient!!.disconnect()
    }

    override fun submitFlatDetails(progress: Int): Any {
//        if (locationAutocomplete.text.toString() == "") {
        return locationAutocomplete.text.toString()
//        }
//        return false
    }

    override fun getProgress() = 3

    private fun getLatLongFromPlace(place: String): LatLng {
        try {
            val selected_place_geocoder = Geocoder(context)
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

    override fun reset() {
        map!!.clear()
        locationAutocomplete.setText("")
    }

    override fun validateInputs() = locationAutocomplete.text.toString() != ""
}

package com.arpan.collegebroker


import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_flat.*
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import java.io.IOException


class CreateFlatFragment : Fragment() {

    val PICK_IMAGE_REQUEST = 11

    var mGoogleApiClient: GoogleApiClient? = null
    var mPlaceAutoCompleteAdapter: PlaceAutoCompleteAdapter? = null
    var mImageAdapter: FlatImageAdapter? = null

    private val LAT_LNG_BOUNDS = LatLngBounds(
            LatLng(-40.0, -168.0), LatLng(71.0, 136.0))

    private lateinit var uploadPhotoFab: FloatingActionButton
    private lateinit var mainFab: FloatingActionButton
    private lateinit var uploadContactFab: FloatingActionButton

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val flatsReference = firebaseDatabase.getReference("flats")

    private val localImageUris = ArrayList<Uri>()

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
        return inflater.inflate(R.layout.fragment_create_flat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFabs()

        mPlaceAutoCompleteAdapter = PlaceAutoCompleteAdapter(context, mGoogleApiClient, LAT_LNG_BOUNDS, null)
        nameEditText.setAdapter(mPlaceAutoCompleteAdapter)

        mImageAdapter = FlatImageAdapter(context!!, localImageUris)
        galleryRecyclerView.adapter = mImageAdapter
        galleryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        initListeners()
    }

    private fun initListeners() {
        doubleBedET.onFocusChangeListener = AutoHideFabListener()
        singleBedET.onFocusChangeListener = AutoHideFabListener()
        tableET.onFocusChangeListener = AutoHideFabListener()
        chairCountET.onFocusChangeListener = AutoHideFabListener()

        furnishingRadioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
//                TODO("DISPLAY CONTENT IF FURNISHED")
            }

        })

        uploadPhotoFab.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
//            TODO("UPLOAD")
        }
    }

    private fun initFabs() {
//        uploadContactFab = activity?.findViewById(R.id.uploadContactFab)!!
//        uploadPhotoFab = activity?.findViewById(R.id.uploadPhotoFab)!!
        mainFab = activity?.findViewById(R.id.mainFab)!!
    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient!!.disconnect()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//
//        (activity as CreateFlatActivity).submitCallbackListener = this
    }

//    override fun submitFlatDetails() {
//        val furnishing = Furnishing(
//                sofaCheckBox.isChecked,
//                tvCheckBox.isChecked,
//                fridgeCheckBox.isChecked,
//                wmCheckBox.isChecked,
//                doubleBedET.text.toString().toInt(),
//                singleBedET.text.toString().toInt(),
//                tableET.text.toString().toInt(),
//                chairCountET.text.toString().toInt())
//
//        val flat = Flat(nameEditText.text.toString(), bhkEditText.text.toString(), priceEditText.text.toString().toInt(), furnishing)
//
//        flatsReference.push().setValue(flat)
//    }

    inner class AutoHideFabListener: View.OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hasFocus) {
                uploadContactFab.hide()
                uploadPhotoFab.hide()
                mainFab.hide()
            }

            else {
                uploadContactFab.show()
                uploadPhotoFab.show()
                mainFab.show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val filePath: Uri = data.data
            try {
                localImageUris.add(filePath)
                photoGalleryHolder.visibility = View.VISIBLE
                galleryRecyclerView.adapter.notifyDataSetChanged()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

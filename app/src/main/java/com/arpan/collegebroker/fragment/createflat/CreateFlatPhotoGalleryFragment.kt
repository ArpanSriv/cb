package com.arpan.collegebroker.fragment.createflat


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arpan.collegebroker.Contact
import com.arpan.collegebroker.CreateFlatActivity
import com.arpan.collegebroker.R
import com.arpan.collegebroker.adapter.ContactsAdapter
import com.arpan.collegebroker.adapter.FlatImagesAdapter
import kotlinx.android.synthetic.main.fragment_create_flat_photo_gallery.*
import kotlinx.android.synthetic.main.item_image.*
import java.io.IOException
import android.content.ContentUris
import android.content.pm.PackageManager
import android.provider.ContactsContract.PhoneLookup
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class CreateFlatPhotoGalleryFragment : Fragment(), CreateFlatActivity.SubmitCallbackListener {

    private val PICK_IMAGE_REQUEST: Int = 99
    private val RESULT_PICK_CONTACT: Int = 98
    private lateinit var mPhotoGalleryAdapter: FlatImagesAdapter
    private lateinit var mContactsAdapter: ContactsAdapter
    private val imageUris = ArrayList<Uri>()
    private val contacts = ArrayList<Contact>()
    private var initialStateChanged_Photos = false
    var initialStateChanged_Contacts = false
    private val CONTACT_PERMISSION_CODE = 99

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_flat_photo_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ((activity) as CreateFlatActivity).submitCallbackListener = this
        uploadPhotosButton.setOnClickListener {
            initialStateChanged_Photos = true
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
//            TODO("UPLOAD")
        }

        uploadContactsButton.setOnClickListener {
            initialStateChanged_Contacts = true
            getPermissions()

            val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT)
        }

        initImageWidth()

        mPhotoGalleryAdapter = FlatImagesAdapter(context!!, imageUris)
        photoRecyclerView.adapter = mPhotoGalleryAdapter
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        mContactsAdapter = ContactsAdapter(context!!, contacts)
        contactsRecyclerView.adapter = mContactsAdapter
        contactsRecyclerView.layoutManager = LinearLayoutManager(context)

    }

    private fun getPermissions() {
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity,
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    CONTACT_PERMISSION_CODE)
        }
    }

    private fun initImageWidth() {
        val iDisplayWidth = resources.displayMetrics.widthPixels
        val resources = context!!.resources
        val metrics = resources.displayMetrics
        var dp = iDisplayWidth / (metrics.densityDpi / 160f)

        if (dp < 360) {
            dp = (dp - 17) / 2
            val px = dpToPx(dp.toInt())
            galleryImage.layoutParams.width = (Math.round(px.toDouble()).toInt())
            galleryImage.requestLayout()
        }
    }

    override fun submitFlatDetails(progress: Int): Any {
        return kotlin.Pair(imageUris, contacts)
    }

    override fun getProgress() = 5

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    if (data != null && data.data != null) {
                        val filePath: Uri = data.data
                        try {
                            imageUris.add(filePath)
                            photoRecyclerView.adapter.notifyDataSetChanged()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                RESULT_PICK_CONTACT -> {
                    updateContactsList(data!!)
                }
            }
        }
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics = context!!.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    private fun updateContactsList(data: Intent) {
        var cursor: Cursor? = null
        try {
            // getData() method will have the Content Uri of the selected contact
            val uri = data.data
            //Query the content uri
            cursor = context!!.contentResolver.query(uri, null, null, null, null)
            cursor!!.moveToFirst()
            // column index of the phone number
            val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            // column index of the contact location
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

            val phoneNo = cursor.getString(phoneIndex)
            val name = cursor.getString(nameIndex)
            val picUri = getPhotoUri(fetchContactIdFromPhoneNumber(phoneNo))

            val contact = Contact(name, phoneNo, picUri.toString())
            contacts.add(contact)
            mContactsAdapter.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun fetchContactIdFromPhoneNumber(phoneNumber: String): Long {
        val uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber))
        val cursor = context!!.contentResolver.query(uri,
                arrayOf(PhoneLookup.DISPLAY_NAME, PhoneLookup._ID), null, null, null)

        var contactId = ""

        if (cursor.moveToFirst()) {
            do {
                contactId = cursor.getString(cursor
                        .getColumnIndex(PhoneLookup._ID))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return contactId.toLong()
    }

    fun getPhotoUri(contactId: Long): Uri? {
        val contentResolver = context!!.contentResolver

        try {
            val cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI, null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null)

            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null // no photo
                }
            } else {
                return null // error in cursor process
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId)
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
    }

    override fun reset() {
        imageUris.clear()
        contacts.clear()

        mContactsAdapter.notifyDataSetChanged()
        mPhotoGalleryAdapter.notifyDataSetChanged()
    }

    override fun validateInputs() = initialStateChanged_Contacts and initialStateChanged_Photos
}

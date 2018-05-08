package com.arpan.collegebroker.fragment.createflat


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.arpan.collegebroker.Flat
import com.arpan.collegebroker.Furnishing
import com.arpan.collegebroker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.android.synthetic.main.fragment_end.*


class EndFragment : Fragment() {
    private lateinit var flat: Flat

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val flatsReference = firebaseDatabase.getReference("flats")
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseStorageRef = firebaseStorage.reference.child("flat_images")
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val uploadedImagesPath = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_end, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        flat = arguments!!.getSerializable(FLAT_KEY) as Flat

        typeValue.text = if (flat.type == 1) "Flat" else "Villa"
        sizeValue.text = "${flat.bhk} BHK"
        locationValue.text = flat.location
        furnishingValue.text = generateFurnishingDetails(flat.furnishing)
        detailsValue.text = "${flat.images.size} image(s) & ${flat.contacts.size} contact(s)"
        descriptionValue.text = flat.description
        costValue.text = "₹" + flat.price + " per month"

        completeFab.setOnClickListener {
            uploadFlat(flat)
        }
    }

    private fun uploadFlat(flat: Flat) {

        endLabel.visibility = INVISIBLE
        finalDetailsHolder.visibility = INVISIBLE

        uploadingLabel.visibility = VISIBLE
        progressBar.visibility = VISIBLE

        flat.flatId = flatsReference.push().key

        flatsReference.child(flat.flatId).setValue(flat)
                .addOnCompleteListener {
                    for (image in flat.images) {
                        val uploadTask = firebaseStorageRef.putFile(Uri.parse(image))

                        uploadTask
                                .addOnProgressListener {
                                    val progress = 100.0 * it.bytesTransferred / it.totalByteCount
                                    if (progressBar.isIndeterminate) progressBar.isIndeterminate = false
                                    progressBar.progress = progress.toInt()
                                    println(progress)
                                    uploadingLabel.text = "Uploading..."
                                }
                                .addOnFailureListener {
                                    val errorCode = (it as StorageException).errorCode
                                    val errorMessage = it.message
                                    // test the errorCode and errorMessage, and handle accordingly

                                    AlertDialog.Builder(context)
                                            .setTitle("ERROR")
                                            .setMessage("Error Code: $errorCode Message: $errorMessage")
                                            .setNeutralButton("OK", { _, _ -> })
                                            .show()
                                }

                        val urlTask = uploadTask.continueWithTask({ task ->
                            if (!task.isSuccessful) {
                                throw task.exception!!
                            }
                            // Continue with the task to get the download URL
                            firebaseStorageRef.downloadUrl
                        }).addOnCompleteListener({ task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                println(downloadUri)
                                flat.imagesLink.add(downloadUri.toString())
                                flatsReference.child(flat.flatId).updateChildren(mapOf("imagesLink" to flat.imagesLink))
                                uploadingLabel.text = "Uploaded."

                            } else {
                                // Handle failures
                                // ...
                                println("Some failure" + task.exception)
                            }
                        })
                    }
                    //Attach this link to current User's Flats
                    val usersFlats = ArrayList<String>()

                    firebaseDatabase.reference.child("users").child(currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError?) {
                            Toast.makeText(context, "Error: ${p0!!.code}", Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach {
                                if (it.key == "listedFlats") {
                                    usersFlats.clear()
                                    usersFlats.addAll(it.value!! as ArrayList<String>)
                                }
                            }

                            usersFlats.add(flat.flatId)
                            firebaseDatabase.reference.child("users").child(currentUser!!.uid).updateChildren(mapOf("listedFlats" to usersFlats))
                        }
                    })
                }

    }

    private fun generateFurnishingDetails(furnishing: Furnishing?): String {
        return if (
                furnishing!!.tv == 0 &&
                furnishing.sofas == 0 &&
                furnishing.chairCount == 0 &&
                furnishing.washingMachine == 0 &&
                furnishing.tableCount == 0 &&
                furnishing.singleBedCount == 0 &&
                furnishing.doubleBedCount == 0 &&
                furnishing.fridge == 0) {
            "Unfurnished"
        } else {
            "Furnished (" +
                    "${furnishing.sofas} Sofa(s), " +
                    "${furnishing.tv} TV(s), " +
                    "${furnishing.fridge} Fridge(s), " +
                    "${furnishing.washingMachine} Washing Machine(s), " +
                    "${furnishing.tableCount} Table(s), " +
                    "${furnishing.chairCount} Chair(s) ," +
                    "${furnishing.singleBedCount} Single Bed(s), " +
                    "${furnishing.doubleBedCount} Double Bed(s)"
        }
    }

    companion object {
        const val FLAT_KEY = "FLAT_KEY"

        @JvmStatic
        fun newInstance(flat: Flat): EndFragment {
            return EndFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(FLAT_KEY, flat)
                }
            }
        }
    }

}

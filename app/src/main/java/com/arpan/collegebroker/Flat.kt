package com.arpan.collegebroker

import java.io.Serializable

class Flat (var flatId: String = "",
            var location: String = "",
            var bhk: Int = -1,
            var type: Int = 0,
            var description: String = "",
            var price: String = "",
            var images: ArrayList<String> = java.util.ArrayList(),
            var imagesLink: ArrayList<String> = ArrayList(),
            var contacts: ArrayList<Contact> = java.util.ArrayList(),
            var furnishing: Furnishing? = null,
            var soldOut: Boolean = false,
            var favouriteFlatIds: ArrayList<String> = ArrayList()): Serializable {
}
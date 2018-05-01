package com.arpan.collegebroker

class Flat (var name: String = "",
            var bhk: Int = -1,
            var type: Int = 0,
            val description: String = "",
            val price: Int = 0,
            var furnishing: Furnishing? = null) {
}
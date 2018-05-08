package com.arpan.collegebroker

import java.io.Serializable

class User (var email: String = "",
            var uid: String = "",
            var name: String = "",
            var listedFlats: ArrayList<String> = ArrayList()) : Serializable
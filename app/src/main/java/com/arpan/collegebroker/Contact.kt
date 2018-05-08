package com.arpan.collegebroker

import android.net.Uri
import java.io.Serializable

class Contact (val name: String = "",
               val number: String = "",
               val picUri: String? = ""): Serializable
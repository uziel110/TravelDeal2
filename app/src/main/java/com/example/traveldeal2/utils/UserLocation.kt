package com.example.traveldeal2.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import java.io.IOException
import java.util.*

class UserLocation() {
    var lat: Double = 0.0
        private set
    var lon: Double = 0.0
        private set

    constructor(_lat: Double, _lon: Double) : this() {
        lat = _lat
        lon = _lon
    }

    fun convertFromLocation(location: Location?): UserLocation? {
        return if (location == null) null else UserLocation(location.latitude, location.longitude)
    }

    fun locationFromAddress(applicationContext: Context, departureAddress: String): UserLocation? {
        lateinit var travelLocation: Location
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        try {
            val l: List<Address> = geocoder.getFromLocationName(departureAddress, 1)
            if (l.isNotEmpty()) {
                val temp: Address = l[0]
                travelLocation = Location("travelLocation")
                travelLocation.latitude = temp.latitude
                travelLocation.longitude = temp.longitude

            } else {
                //Toast.makeText(this, "4:Unable to understand address", Toast.LENGTH_LONG).show()
                return null
            }
        } catch (e: IOException) {
//            Toast.makeText(
//                this,
//                "5:Unable to understand address. Check Internet connection.",
//                Toast.LENGTH_LONG
//            ).show()
            return null
        }

        return convertFromLocation(travelLocation)
    }

    companion object {
        fun addressFromLocation(applicationContext: Context, userLocation: UserLocation): String? {
            val addresses: List<Address>
            val geocoder = Geocoder(applicationContext, Locale.getDefault())

            addresses = geocoder.getFromLocation(userLocation.lat, userLocation.lon, 1)

            val address =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

//            val city = addresses[0].locality
//            val state = addresses[0].adminArea
//            val country = addresses[0].countryName
//            val postalCode = addresses[0].postalCode
//            val knownName = addresses[0].featureName // Only if available else return NULL


            return address
        }
    }
}


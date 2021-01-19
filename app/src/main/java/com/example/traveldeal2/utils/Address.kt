package com.example.traveldeal2.utils

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import kotlin.math.*


class AddressTool {
    companion object {
        //getLocation From Address
        fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
            val coder = Geocoder(context)
            val address: List<*>?
            var latLong: LatLng? = null
            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null) {
                    return null
                }
                val location = address[0]
                latLong = LatLng(location.latitude, location.longitude)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return latLong
        }

        private const val AVERAGE_RADIUS_OF_EARTH = 6371.0
        fun calculateDistance(
            userLatLong: LatLng,
            venueLatLng: LatLng
        ): Float {
            val userLat = userLatLong.latitude
            val userLng = userLatLong.longitude
            val venueLat = venueLatLng.latitude
            val venueLng = venueLatLng.longitude
            val latDistance = Math.toRadians(userLat - venueLat)
            val lngDistance = Math.toRadians(userLng - venueLng)
            val a = sin(latDistance / 2) * sin(latDistance / 2) +
                    cos(Math.toRadians(userLat)) *
                    cos(Math.toRadians(venueLat)) *
                    sin(lngDistance / 2) *
                    sin(lngDistance / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return ((AVERAGE_RADIUS_OF_EARTH * c).roundToInt()).toFloat()
        }
    }
}
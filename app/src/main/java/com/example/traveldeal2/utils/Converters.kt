package com.example.traveldeal2.utils

import androidx.room.TypeConverter
import com.example.traveldeal2.enums.Status
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class UserLocationConverter {
    @TypeConverter
    fun fromString(value: String?): UserLocation? {
        if (value == null || value == "") return null
        val lat = value.split(" ").toTypedArray()[0].toDouble()
        val lang = value.split(" ").toTypedArray()[1].toDouble()
        return UserLocation(lat, lang)
    }

    @TypeConverter
    fun asString(userLocation: UserLocation?): String {
        return if (userLocation == null) "" else userLocation.lat
            .toString() + " " + userLocation.lon
    }
}

class DestinationAddresses {
    @TypeConverter
    fun getString(addressesList: MutableList<String>): String {
        val addressesString = StringBuilder()
        for (point in addressesList)
            addressesString.append("$point&")
        return addressesString.toString()
    }

    @TypeConverter
    fun getListFromString(addressesString: String): MutableList<String> {
        return addressesString.split("&").toMutableList()
    }
}

class RequestStatusConverter {

    @TypeConverter
    fun getTypeInt(status: Status) = status.ordinal

    @TypeConverter
    fun getStatus(value: Int) = enumValues<Status>()[value]

//    @TypeConverter
//    fun getStatus(status: Int): Status? {
//        return when (status) {
//            0 -> Status.SENT
//            1 -> Status.RECEIVED
//            2 -> Status.RUNNING
//            3 -> Status.CLOSED
//            4 -> Status.PAID
//            else -> null
//        }
//    }
}

//object CompanyConverter {
//    @TypeConverter
//    fun fromString(value: String?): Map<String, Boolean> {
//        val mapType: Type = object : TypeToken<Map<String?, Boolean?>?>() {}.type
//        return Gson().fromJson(value, mapType)
//    }
//
//    @TypeConverter
//    fun fromStringMap(map: Map<String?, Boolean?>?): String {
//        val gson = Gson()
//        return gson.toJson(map)
//    }
//}

class CompanyConverter {
    @TypeConverter
    fun fromString(value: String?): MutableMap<String, Boolean>? {
        if (value == null || value.isEmpty()) return null
        val mapString =
            value.split(",").toTypedArray() //split map into array of (string,boolean) strings
        val hashMap: MutableMap<String, Boolean> = HashMap()
        for (s1 in mapString)  //for all (string,boolean) in the map string
        {
            if (s1.isNotEmpty()) { //is empty maybe will needed because the last char in the string is ","
                val s2 = s1.split(":")
                    .toTypedArray() //split (string,boolean) to company string and boolean string.
                val aBoolean = java.lang.Boolean.parseBoolean(s2[1])
                hashMap[s2[0]] = aBoolean
            }
        }
        return hashMap
    }

    @TypeConverter
    fun asString(map: MutableMap<String?, Boolean?>?): String? {
        if (map == null) return null
        val mapString = StringBuilder()
        for ((key, value) in map.entries) mapString.append(
            key
        ).append(":").append(value).append(",")
        return mapString.toString()
    }
}
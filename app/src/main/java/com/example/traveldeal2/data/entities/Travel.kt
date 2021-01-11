package com.example.traveldeal2.data.entities

import androidx.room.*
import androidx.room.Entity
import com.example.traveldeal2.utils.UserLocation
import com.google.firebase.database.Exclude

//@TypeConverters(Travel.UserLocationConverter::class)
//private val travelLocation: UserLocation? = null

@Entity(tableName = "travels_table")
class Travel {
    @PrimaryKey
    var clientId: String = ""
        set
        get() = field
    var clientName: String = ""
        set
        get() = field
    var clientPhone: String = ""
        set
        get() = field
    var clientEmailAddress: String = ""
        set
        get() = field
    var departureAddress: String = ""
        set
        get() = field
    @TypeConverters(UserLocationConverter::class)
    var departLocation: UserLocation? = null
        set
        get() = if (field != null) field else UserLocation(0.0, 0.0)
    var departureDate: String = ""
        set
        get() = field
    var destinationAddress: String = ""
        set
        get() = field
    @TypeConverters(UserLocationConverter::class)
    var destLocation: UserLocation? = null
        set
        get() = if (field != null) field else UserLocation(0.0, 0.0)
    var returnDate: String = ""
        set
        get() = field
    var passengersNumber: Int = 0
        set
        get() = field
    var requestStatus: String = ""
        set
        get() = field

    // for expandable of card in recycle view
    @Ignore
    var expandable: Boolean = false
        @Exclude
        set
        get() = field

    // temporary ignored
    @Ignore
    var company: HashMap<String, Boolean> = hashMapOf()
        set
        get() = field

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
}
package com.example.traveldeal2.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.utils.UserLocationConverter

@Database(entities = [Travel::class], version = 1, exportSchema = false)
@TypeConverters(UserLocationConverter::class)
abstract class TravelRoomDatabase : RoomDatabase() {

    abstract fun travelDao(): TravelDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: TravelRoomDatabase? = null

        fun getDatabase(context: Context): TravelRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelRoomDatabase::class.java,
                    "travel_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
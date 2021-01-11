package com.example.traveldeal2.data.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.traveldeal2.data.entities.Travel
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Travel::class], version = 1, exportSchema = false)
@TypeConverters(Travel.UserLocationConverter::class)
abstract class TravelRoomDatabase : RoomDatabase() {

    abstract fun travelDao(): TravelDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: TravelRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TravelRoomDatabase {
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
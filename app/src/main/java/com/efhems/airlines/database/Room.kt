package com.efhems.airlines.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.efhems.airlines.domain.Airport

@Database(entities = [Airport::class], version = 1)
abstract class AirlineDatabase : RoomDatabase() {
    abstract val dao: Dao
}

/**
 * create an singleton instance of database
 * */
private lateinit var INSTANCE: AirlineDatabase
fun getDatabase(context: Context): AirlineDatabase {
    synchronized(AirlineDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AirlineDatabase::class.java, "Replace with actual Db Name"
            ).build()
        }
    }
    return INSTANCE
}
package com.efhems.airlines.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.efhems.airlines.domain.Airport

@Database(entities = [Airport::class], version = 1)
abstract class FootballDatabase : RoomDatabase() {
    abstract val dao: Dao
}

private lateinit var INSTANCE: FootballDatabase
fun getDatabase(context: Context): FootballDatabase {
    synchronized(FootballDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                FootballDatabase::class.java, "Replace with actual Db Name"
            ).build()
        }
    }
    return INSTANCE
}
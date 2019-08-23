package com.efhems.airlines.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efhems.airlines.domain.Airport

@Dao
interface Dao {

    /**
     * dao method, inserting all the airport in to DB
    * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAirports(airports: List<Airport>)

    /**
     * dao method, reading all the airport in to DB
     * */
    @Query("SELECT * FROM airport")
    fun airports(): LiveData<List<Airport>>
}
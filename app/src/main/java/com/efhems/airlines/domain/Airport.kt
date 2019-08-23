package com.efhems.airlines.domain

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for airport.
 */
@Entity
data class Airport(

    /**
     * 3-letter code for the airport. name is unique
    * */
    @PrimaryKey
    @NonNull
    var name: String,
    /**
     * latitude and longitude coordinate for this airport
    * */
    val latitude: Long,
    val longitude: Long,
    val nextLink: String
    )

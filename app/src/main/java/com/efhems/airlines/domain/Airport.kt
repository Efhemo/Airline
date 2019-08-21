package com.efhems.airlines.domain

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Airport(
    @PrimaryKey
    @NonNull
    var name: String,
    val latitude: Long,


    val longitude: Long)

/*data class Flight(
    @Json(name = "AirportResource")
    val airports: Airports
)

data class Airports(
    @Json(name = "Airports")
    val airport: Airport
)

data class AllAirport(
    @Json(name = "AirportCode")
    val airportCode: String,
    @Json(name = "Position")
    val position: Position
)

data class Airport(
    @Json(name = "Airport")
    val airport: List<AllAirport>
)

data class Position(
    @Json(name = "Coordinate")
    val coordinate: Coordinate
)

data class Coordinate(
    @Json(name = "Latitude")
    val latitude: Double,
    @Json(name = "Longitude")
    val longitude: Double
)*/


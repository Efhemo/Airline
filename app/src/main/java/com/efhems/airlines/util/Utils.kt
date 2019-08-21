package com.efhems.airlines.util

import com.efhems.airlines.domain.Airport
import org.json.JSONObject

fun extractAirports(x: String): List<Airport> {

    val airports = ArrayList<Airport>()

    val jsonAirports = JSONObject(x).getJSONObject("AirportResource").
        getJSONObject("Airports").getJSONArray("Airport")

    for (i in 0 until jsonAirports.length()){
        val airport = jsonAirports.getJSONObject(i)

        val coordinate = airport.getJSONObject("Position").getJSONObject("Coordinate")

        airports.add(Airport(
            airport.getString("AirportCode"),
            coordinate.getLong("Latitude"),
            coordinate.getLong("Longitude")))

    }

    return airports

}
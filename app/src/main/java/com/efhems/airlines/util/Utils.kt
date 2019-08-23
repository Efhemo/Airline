package com.efhems.airlines.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.efhems.airlines.domain.Airport
import com.efhems.airlines.domain.Schedule
import org.json.JSONObject

/**
 * Method for extracting Airpot
 * */
fun extractAirports(x: String): List<Airport> {

    val airports = ArrayList<Airport>()

    val jsonAirportResource = JSONObject(x).getJSONObject("AirportResource")

    val jsonAirports = jsonAirportResource.getJSONObject("Airports").getJSONArray("Airport")

    val links = jsonAirportResource.getJSONObject("Meta").getJSONArray("Link")

    var nextLink = ""

    for (i in 0 until links.length()){
        val isNext = links.getJSONObject(i).getString("@Rel")

        if(isNext == "next"){
           nextLink = links.getJSONObject(i).getString("@Href")
        }
    }


    for (i in 0 until jsonAirports.length()) {
        val airport = jsonAirports.getJSONObject(i)

        val coordinate = airport.getJSONObject("Position").getJSONObject("Coordinate")

        airports.add(
            Airport(
                airport.getString("AirportCode"),
                coordinate.getLong("Latitude"),
                coordinate.getLong("Longitude"),
                nextLink
            )
        )
    }
    return airports
}

/**
 * Method for extracting Schedules
 * */
fun extractSchedule(x: String): List<Schedule> {

    val schedules = ArrayList<Schedule>()
    val jsonAirports = JSONObject(x).getJSONObject("ScheduleResource").optJSONArray("Schedule")

    if (jsonAirports == null) {
        val jsonSchedule = JSONObject(x).getJSONObject("ScheduleResource").getJSONObject("Schedule")
        val schedule = schedule(jsonSchedule)

        schedules.add(schedule)

    } else {


        for (i in 0 until jsonAirports.length()) {
            val airport = jsonAirports.getJSONObject(i)

            val schedule = schedule(airport)

            schedules.add(schedule)
        }
    }

    return schedules
}

private fun schedule(airport: JSONObject): Schedule {
    val coordinate = airport.getJSONObject("Flight")
    val dDateTimme =
        coordinate.getJSONObject("Departure").getJSONObject("ScheduledTimeLocal").getString("DateTime")
    val aDateTimme =
        coordinate.getJSONObject("Arrival").getJSONObject("ScheduledTimeLocal").getString("DateTime")
    val flightNumber = coordinate.getJSONObject("MarketingCarrier").getLong("FlightNumber")
    val daysOfOperation = coordinate.getJSONObject("Details").getLong("DaysOfOperation")

    val schedule = Schedule(dDateTimme, aDateTimme, flightNumber, daysOfOperation)
    return schedule
}

class Time(internal var hours: Int, internal var minutes: Int, internal var seconds: Int)

fun timeDifference(start: Time, stop: Time): Time {
    val diff = Time(0, 0, 0)
    if (stop.seconds > start.seconds) {
        --start.minutes
        start.seconds += 60
    }
    diff.seconds = start.seconds - stop.seconds
    if (stop.minutes > start.minutes) {
        --start.hours
        start.minutes += 60
    }
    diff.minutes = start.minutes - stop.minutes
    diff.hours = start.hours - stop.hours

    return diff
}

fun hideKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}
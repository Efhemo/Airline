package com.efhems.airlines.domain

/**
 * Data class for Schedules.
 */
data class Schedule(
    val departureTime: String,
    val arrivalTime: String,
    val flightNumber: Long,
    val dayOfOperation: Long
)
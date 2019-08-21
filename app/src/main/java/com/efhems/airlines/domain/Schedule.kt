package com.efhems.airlines.domain

data class Schedule(
    val departureTime: String,
    val arrivalTime: String,
    val flightNumber: Long,
    val dayOfOperation: Long
)
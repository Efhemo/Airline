package com.efhems.airlines.repository

import android.util.Log
import com.efhems.airlines.database.FootballDatabase
import com.efhems.airlines.network.Network
import com.efhems.airlines.util.extractAirports
import com.efhems.airlines.util.extractSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

const val offset = 0
const val lhOperated = 0
const val token = "5mnd44b2ajrn7ucykhqtrsqu"

class Repository(private val database: FootballDatabase) {

    private val TAG = Repository::class.java.name

    suspend fun airports() {

        withContext(Dispatchers.IO) {
            try {
                val response = Network.service.airports("Bearer $token",
                    offset,
                    lhOperated
                )
                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!.string()
                    val airports = extractAirports(body)

                    Log.i(TAG, "size is $body")
                    database.dao.insertAirports(airports)
                } else {
                    Log.i(TAG, "size is empty "+ response.code())
                    null
                }
            } catch (e: HttpException) {
                Log.i(TAG, "size exception here too ${e.message()}")

                null
            } catch (e: Exception) {
                Log.i(TAG, "size exception here ${e.message}")

                null
            }
        }
    }

    suspend fun schedules(origin: String, destinatin: String, fraomDate: String) =

        withContext(Dispatchers.IO){

            try {
                val response = Network.service.schedules("Bearer $token", origin, destinatin, fraomDate, 0)

                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!.string()
                    val schedules = extractSchedule(body)
                    schedules
                } else {
                    Log.i(TAG, "size is empty " + response.code())
                    null
                }
            }catch (e: Exception){
                Log.i(TAG, "size is empty exception" + e.message)
                null
            }

        }



    //database
    fun getAirports() = database.dao.airports()

}
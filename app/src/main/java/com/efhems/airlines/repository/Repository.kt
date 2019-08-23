package com.efhems.airlines.repository

import com.efhems.airlines.database.AirlineDatabase
import com.efhems.airlines.network.Network
import com.efhems.airlines.util.extractAirports
import com.efhems.airlines.util.extractSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

const val offset = 0
const val lhOperated = 0
const val limit = 100
const val token = "9k6u8fkynv6rf6kwmcbbf8wb"

/**
 * Source class for getting airport data from remote and local data sets
 */
class Repository(private val database: AirlineDatabase) {


    /**
     * Gets airport details from API.
     */
    suspend fun airports() {

        withContext(Dispatchers.IO) {
            try {
                val response = Network.service.airports("Bearer $token",
                    limit,
                    offset,
                    lhOperated
                )
                if (response.isSuccessful && response.body() != null) {

                    /**
                     * Get whole response in string
                     * */
                    val body = response.body()!!.string()

                    /**
                     * Extract needed data instead of [moshi] converting whole data
                     * */
                    val airports = extractAirports(body)

                    /**
                     * insert return data into database
                     * */
                    database.dao.insertAirports(airports)
                    if (airports[0].nextLink.isNotEmpty()) {
                        airportsNext(airports[0].nextLink)
                    }
                    else return@withContext
                } else {

                    /**
                     * if anything happens on the way just return null. it will be taken care of
                     * */
                    null
                }
            } catch (e: HttpException) {
                null
            } catch (e: Exception) {
                null
            }
        }
    }


    /**
     * Recursive function for getting next paging airport details from API.
     */
    private suspend fun airportsNext(next: String) {

        withContext(Dispatchers.IO) {
            try {
                val response = Network.service.airportsNext("Bearer $token",
                    next
                )
                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!.string()
                    val airports = extractAirports(body)

                    /**
                     * insert return data into database
                     * */
                    database.dao.insertAirports(airports)

                    /**
                     *if next url is available. get next airport details
                     * */
                    if (airports[0].nextLink.isNotEmpty()){
                        airportsNext(airports[0].nextLink)
                    } else return@withContext
                } else {
                    null
                }
            } catch (e: HttpException) {
                null
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Gets flight schedules from origin to destination from API.
     */
    suspend fun schedules(origin: String, destinatin: String, fraomDate: String) =

        withContext(Dispatchers.IO){

            try {
                val response = Network.service.schedules("Bearer $token", origin, destinatin, fraomDate, 0)

                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!.string()
                    val schedules = extractSchedule(body)
                    schedules
                } else {
                    null
                }
            }catch (e: Exception){
                null
            }
        }

    /**
     * Get all airport from database
     * */
    fun getAirports() = database.dao.airports()

}
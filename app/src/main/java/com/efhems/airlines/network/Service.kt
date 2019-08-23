package com.efhems.airlines.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface Service {

    /**
     * [GET] Api Service function that get airports to coordinates.
     * @return Kotlin Coroutines response object.
     */
    @Headers("Accept: application/json")
    @GET("mds-references/airports/")
    suspend fun airports(@Header("Authorization") auth: String,
                         @Query("limit") limit: Int,
                         @Query("offset") offset: Int,
                         @Query("LHoperated") lhOperated: Int): Response<ResponseBody>


    /**
     * [GET] Api Service function that takes care of the paging request
     * @param auth require token to access endpoint
     * @param next url to query
     * @return Kotlin Coroutines response object.
     */
    @Headers("Accept: application/json")
    @GET
    suspend fun airportsNext(@Header("Authorization") auth: String,
                         @Url next: String): Response<ResponseBody>


    /**
     * [GET] Api Service function that gets available flight schedules between airports
     * @param auth require token to access endpoint
     * @param origin Airport code of origin airport.
     * @param destination Airport code of destination airport.
     * @param fromDate Local departure date.
     * @return Kotlin Coroutines response object.
     */
    @Headers("Accept: application/json")
    @GET("operations/schedules/{origin}/{destination}/{fromDate}")
    suspend fun schedules(@Header("Authorization") auth: String,
                          @Path("origin") origin: String,
                          @Path("destination") destination: String,
                          @Path("fromDate") fromDate: String,
                          @Query("directFlights") directFlights: Int): Response<ResponseBody>
}
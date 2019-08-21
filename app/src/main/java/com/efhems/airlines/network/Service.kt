package com.efhems.airlines.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface Service {

    @Headers("Accept: application/json")
    @GET("mds-references/airports/")
    suspend fun airports(@Header("Authorization") auth: String,
                         @Query("offset") offset: Int, @Query("LHoperated") lhOperated: Int):
            Response<ResponseBody>

    @Headers("Accept: application/json")
    @GET("operations/schedules/{origin}/{destination}/{fromDate}")
    suspend fun schedules(@Header("Authorization") auth: String,
                          @Path("origin") origin: String,
                          @Path("destination") destination: String,
                          @Path("fromDate") fromDate: String,
                          @Query("directFlights") directFlights: Int
    ):
            Response<ResponseBody>
}
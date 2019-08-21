package com.efhems.airlines.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface Service {

    @Headers("Accept: application/json")
    @GET("mds-references/airports/")
    suspend fun airports(@Header("Authorization") auth: String,
                         @Query("offset") offset: Int, @Query("LHoperated") lhOperated: Int):
            Response<ResponseBody>

   /* @Headers("Accept: application/json")
    @GET("mds-references/airports/")
    suspend fun schedules(@Header("Authorization") auth: String,
                         @Query("offset") offset: Int, @Query("LHoperated") lhOperated: Int):
            Response<ResponseBody>*/
}
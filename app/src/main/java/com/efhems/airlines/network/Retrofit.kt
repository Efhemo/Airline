package com.efhems.airlines.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Base Api url for Lufthansa.
 */
private const val BASE_URL: String = "https://api.lufthansa.com/v1/"

/**
 * setup Moshi-kotlin converter factory
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * okkhttp logging interceptor into Logcat for debugging purpose
 */
val interceptor = HttpLoggingInterceptor()
val client: OkHttpClient = OkHttpClient.Builder().
    addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build()

object Network {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    /**
     * [service] Provides retrofit instance
     */
    val service: Service = retrofit.create(Service::class.java)
}
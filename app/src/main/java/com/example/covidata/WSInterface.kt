package com.example.covidata

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WSInterface {
    @GET("countries")
    fun getAllCountries() : Call<List<Country>>

    @GET("world/total")
    fun getGlobalData() : Call<Global>

    @GET("total/country/{country}")
    fun getCountryData(@Path("country") country: String,
                       @Query("from") from: String,
                       @Query("to") to: String) : Call<List<CountryStat>>
}
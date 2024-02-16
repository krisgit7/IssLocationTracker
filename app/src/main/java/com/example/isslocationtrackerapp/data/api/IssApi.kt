package com.example.isslocationtrackerapp.data.api

import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData
import retrofit2.http.GET

interface IssApi {

    @GET("iss-now.json")
    suspend fun getCurrentLocation(): IssLocationData

    @GET("astros.json")
    suspend fun getPassengers(): IssPassengerData
}
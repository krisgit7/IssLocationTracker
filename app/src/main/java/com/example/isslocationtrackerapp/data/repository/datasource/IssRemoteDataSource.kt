package com.example.isslocationtrackerapp.data.repository.datasource

import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData

interface IssRemoteDataSource {

    suspend fun getCurrentLocationData(): IssLocationData
    suspend fun getPassengers(): IssPassengerData
}
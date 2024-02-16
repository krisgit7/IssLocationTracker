package com.example.isslocationtrackerapp.data.repository

import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData
import kotlinx.coroutines.flow.Flow

interface IssDataRepository {

    fun getCurrentLocation(): Flow<List<IssLocationData>>
    fun getLocationsFromDb(): Flow<List<IssLocationData>>

    fun getLocationsFromDb(interval: Long): Flow<List<IssLocationData>>
    suspend fun saveCurrentLocation(issLocationData: IssLocationData)
    suspend fun getPassengers(): IssPassengerData

    fun getPassengerFlow(interval: Long): Flow<IssPassengerData>
}
package com.example.isslocationtrackerapp.data.repository.datasource

import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData
import kotlinx.coroutines.flow.Flow

interface IssLocalDataSource {

    fun getIssLocationsFromDb(): Flow<MutableList<IssLocationData>>
    suspend fun getIssLocationsFromDbAfterId(id: Int): List<IssLocationData>
    suspend fun getIssPassengersFromDb(): IssPassengerData
    suspend fun saveIssLocationToDb(issLocationData: IssLocationData)
    suspend fun saveIssPassengerToDb(issPassengerData: IssPassengerData)
}
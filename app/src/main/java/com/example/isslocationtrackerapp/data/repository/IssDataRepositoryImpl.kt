package com.example.isslocationtrackerapp.data.repository

import android.util.Log
import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData
import com.example.isslocationtrackerapp.data.repository.datasource.IssLocalDataSource
import com.example.isslocationtrackerapp.data.repository.datasource.IssRemoteDataSource
import com.example.isslocationtrackerapp.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import retrofit2.HttpException

class IssDataRepositoryImpl(
    private val issLocalDataSource: IssLocalDataSource,
    private val issRemoteDataSource: IssRemoteDataSource
) : IssDataRepository {

    override fun getCurrentLocation(): Flow<List<IssLocationData>> {
        return getLatestIssLocationEveryFiveSeconds()
    }

    override fun getLocationsFromDb(): Flow<List<IssLocationData>> {
        var issLocations: Flow<List<IssLocationData>> = emptyFlow()
        try {
            issLocations = issLocalDataSource.getIssLocationsFromDb()
        } catch (exception: Exception) {
            Log.d(Constants.TAG, exception.message.toString())
        }

        return issLocations
    }

    override fun getLocationsFromDb(interval: Long): Flow<List<IssLocationData>> {
        return getLatestIssLocationInDBEvery(interval)
    }

    override suspend fun saveCurrentLocation(issLocationData: IssLocationData) {
        issLocalDataSource.saveIssLocationToDb(issLocationData)
    }

    override suspend fun getPassengers(): IssPassengerData {
        return issRemoteDataSource.getPassengers()
    }

    override fun getPassengerFlow(interval: Long): Flow<IssPassengerData> {
        return getIssPassengerFlow()
    }

    private fun getIssPassengerFlow(interval: Long = 5_000L) = flow {
        while (true) {
            val issPassengers = issRemoteDataSource.getPassengers()
            emit(issPassengers)
            delay(interval)
        }
    }.retry { cause ->
        val shouldRetry = cause is HttpException

        if (shouldRetry) {
            delay(interval)
        }

        shouldRetry
    }

    private fun getLatestIssLocationInDBEvery(interval: Long = 5_000L) = flow {
        var id = -1
        while (true) {
            val issLocations = getIssLocationsFromDbAfterId(id)
            if (issLocations.isNotEmpty()) {
                emit(issLocations)
                id = issLocations.last().id
            }
            Log.d(Constants.TAG, "getLocationsFromDb after ID: $id ")
            delay(interval)
        }
    }.retry {
        true
    }

    private suspend fun getIssLocationsFromDbAfterId(id: Int): List<IssLocationData> {
        val issLocations = mutableListOf<IssLocationData>()
        try {
            val issLocationData = issLocalDataSource.getIssLocationsFromDbAfterId(id)
            issLocations.addAll(issLocationData)
        } catch (exception: Exception) {
            Log.i(Constants.TAG, exception.message.toString())
        }
        return issLocations
    }

    private fun getLatestIssLocationEveryFiveSeconds() = flow {
        while (true) {
            val issLocations = getIssLocationFromAPI()
            emit(issLocations)
            delay(5_000L)
        }
    }.retry { cause ->
        val shouldRetry = cause is HttpException

        if (shouldRetry) {
            delay(5_000)
        }

        shouldRetry
    }

    private suspend fun getIssLocationFromAPI(): List<IssLocationData> {
        val issLocations = mutableListOf<IssLocationData>()
        try {
            val issLocationData = issRemoteDataSource.getCurrentLocationData()
            issLocations.add(issLocationData)
        } catch (exception: Exception) {
            Log.i(Constants.TAG, exception.message.toString())
        }
        return issLocations
    }
}
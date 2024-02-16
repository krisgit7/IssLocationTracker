package com.example.isslocationtrackerapp.data.repository.datasourceimpl

import com.example.isslocationtrackerapp.data.db.IssLocationDataDao
import com.example.isslocationtrackerapp.data.db.IssPassengerDataDao
import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData
import com.example.isslocationtrackerapp.data.repository.datasource.IssLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class IssLocalDataSourceImpl(
    private val issLocationDataDao: IssLocationDataDao,
    private val issPassengerDataDao: IssPassengerDataDao
) : IssLocalDataSource {

    override fun getIssLocationsFromDb(): Flow<MutableList<IssLocationData>> {
        return issLocationDataDao.getIssLocations()
    }

    override suspend fun getIssLocationsFromDbAfterId(id: Int): List<IssLocationData> {
        return issLocationDataDao.getIssLocationsAfterId(id)
    }

    override suspend fun getIssPassengersFromDb(): IssPassengerData {
        return issPassengerDataDao.getIssPassengers().last()
    }

    override suspend fun saveIssLocationToDb(issLocationData: IssLocationData) {
        CoroutineScope(Dispatchers.IO).launch {
            issLocationDataDao.saveIssLocation(issLocationData)
        }
    }

    override suspend fun saveIssPassengerToDb(issPassengerData: IssPassengerData) {
        CoroutineScope(Dispatchers.IO).launch {
            issPassengerDataDao.saveIssPassenger(issPassengerData)
        }
    }
}
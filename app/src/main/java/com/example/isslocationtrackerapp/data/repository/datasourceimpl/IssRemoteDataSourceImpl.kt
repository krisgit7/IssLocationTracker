package com.example.isslocationtrackerapp.data.repository.datasourceimpl

import com.example.isslocationtrackerapp.data.api.IssApi
import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData
import com.example.isslocationtrackerapp.data.repository.datasource.IssRemoteDataSource

class IssRemoteDataSourceImpl(
    private val api: IssApi
) : IssRemoteDataSource {
    override suspend fun getCurrentLocationData(): IssLocationData {
        return api.getCurrentLocation()
    }

    override suspend fun getPassengers(): IssPassengerData {
        return api.getPassengers()
    }
}
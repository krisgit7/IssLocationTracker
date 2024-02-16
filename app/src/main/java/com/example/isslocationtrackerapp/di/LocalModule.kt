package com.example.isslocationtrackerapp.di

import com.example.isslocationtrackerapp.data.db.IssLocationDataDao
import com.example.isslocationtrackerapp.data.db.IssPassengerDataDao
import com.example.isslocationtrackerapp.data.repository.datasource.IssLocalDataSource
import com.example.isslocationtrackerapp.data.repository.datasourceimpl.IssLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideIssLocalDataSource(issLocationDataDao: IssLocationDataDao, issPassengerDataDao: IssPassengerDataDao): IssLocalDataSource {
        return IssLocalDataSourceImpl(issLocationDataDao, issPassengerDataDao)
    }
}
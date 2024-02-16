package com.example.isslocationtrackerapp.di

import com.example.isslocationtrackerapp.data.repository.IssDataRepository
import com.example.isslocationtrackerapp.data.repository.IssDataRepositoryImpl
import com.example.isslocationtrackerapp.data.repository.datasource.IssLocalDataSource
import com.example.isslocationtrackerapp.data.repository.datasource.IssRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideIssDataRepository(issLocalDataSource: IssLocalDataSource, issRemoteDataSource: IssRemoteDataSource): IssDataRepository {
        return IssDataRepositoryImpl(issLocalDataSource, issRemoteDataSource)
    }
}
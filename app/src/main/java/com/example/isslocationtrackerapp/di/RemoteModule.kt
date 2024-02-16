package com.example.isslocationtrackerapp.di

import com.example.isslocationtrackerapp.data.api.IssApi
import com.example.isslocationtrackerapp.data.repository.datasource.IssRemoteDataSource
import com.example.isslocationtrackerapp.data.repository.datasourceimpl.IssRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideIssRemoteDataSource(api: IssApi): IssRemoteDataSource {
        return IssRemoteDataSourceImpl(api)
    }
}
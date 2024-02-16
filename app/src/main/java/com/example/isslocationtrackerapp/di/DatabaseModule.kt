package com.example.isslocationtrackerapp.di

import android.app.Application
import androidx.room.Room
import com.example.isslocationtrackerapp.data.db.IssDatabase
import com.example.isslocationtrackerapp.data.db.IssLocationDataDao
import com.example.isslocationtrackerapp.data.db.IssPassengerDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideIssDatabase(app: Application): IssDatabase {
        return Room.databaseBuilder(app, IssDatabase::class.java, "issdatabase")
            .build()
    }

    @Singleton
    @Provides
    fun provideIssLocationDataDao(issDatabase: IssDatabase): IssLocationDataDao {
        return issDatabase.issLocationDataDao()
    }

    @Singleton
    @Provides
    fun provideIssPassengerDataDao(issDatabase: IssDatabase): IssPassengerDataDao {
        return issDatabase.issPassengerDataDao()
    }

}
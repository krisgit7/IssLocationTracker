package com.example.isslocationtrackerapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData

@Database(
    entities = [IssLocationData::class, IssPassengerData::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class IssDatabase : RoomDatabase() {
    abstract fun issLocationDataDao(): IssLocationDataDao
    abstract fun issPassengerDataDao(): IssPassengerDataDao
}
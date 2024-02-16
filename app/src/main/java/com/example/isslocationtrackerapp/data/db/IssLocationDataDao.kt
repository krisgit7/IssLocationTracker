package com.example.isslocationtrackerapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.isslocationtrackerapp.data.model.IssLocationData
import kotlinx.coroutines.flow.Flow

@Dao
interface IssLocationDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveIssLocation(issLocationData: IssLocationData)

    @Query("SELECT * FROM iss_locations ORDER BY timestamp ASC")
    fun getIssLocations(): Flow<MutableList<IssLocationData>>
}
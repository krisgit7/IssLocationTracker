package com.example.isslocationtrackerapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.isslocationtrackerapp.data.model.IssPassengerData

@Dao
interface IssPassengerDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveIssPassenger(issPassengerData: IssPassengerData)

    @Query("SELECT * FROM iss_passengers")
    suspend fun getIssPassengers(): List<IssPassengerData>
}
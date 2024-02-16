package com.example.isslocationtrackerapp.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "iss_locations")
data class IssLocationData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("iss_position")
    val issPosition: LocationData,
    @SerializedName("message")
    val message: String,
    @SerializedName("timestamp")
    val timestamp: Int
)
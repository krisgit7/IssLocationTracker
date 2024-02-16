package com.example.isslocationtrackerapp.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "iss_passengers")
data class IssPassengerData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("number")
    val number: Int,
    @SerializedName("people")
    val people: List<People>
)
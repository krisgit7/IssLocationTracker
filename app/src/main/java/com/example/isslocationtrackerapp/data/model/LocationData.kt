package com.example.isslocationtrackerapp.data.model


import com.google.gson.annotations.SerializedName

data class LocationData(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)
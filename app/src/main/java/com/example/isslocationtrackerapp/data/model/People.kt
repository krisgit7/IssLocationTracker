package com.example.isslocationtrackerapp.data.model


import com.google.gson.annotations.SerializedName

data class People(
    @SerializedName("craft")
    val craft: String,
    @SerializedName("name")
    val name: String
)
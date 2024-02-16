package com.example.isslocationtrackerapp.data.db

import androidx.room.TypeConverter
import com.example.isslocationtrackerapp.data.model.LocationData
import com.example.isslocationtrackerapp.data.model.People
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromListOfPeople(peopleList: List<People>): String {
        return Gson().toJson(peopleList)
    }

    @TypeConverter
    fun toListOfPeople(jsonPeopleList: String): List<People> {
        val listType = object : TypeToken<List<People>>() {}.type
        return Gson().fromJson(jsonPeopleList, listType)
    }

    @TypeConverter
    fun fromLocation(locationData: LocationData): String {
        return Gson().toJson(locationData)
    }

    @TypeConverter
    fun toLocation(jsonLocationData: String): LocationData {
        return Gson().fromJson(jsonLocationData, LocationData::class.java)
    }
}
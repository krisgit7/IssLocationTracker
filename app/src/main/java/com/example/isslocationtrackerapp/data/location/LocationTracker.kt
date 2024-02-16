package com.example.isslocationtrackerapp.data.location

import android.location.Location

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?
}
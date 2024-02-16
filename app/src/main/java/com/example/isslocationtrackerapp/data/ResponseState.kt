package com.example.isslocationtrackerapp.data

import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.IssPassengerData

sealed class ResponseState {

    data class SuccessPassenger(val issPassengerData: IssPassengerData) : ResponseState()
    data class SuccessLocation(val issLocations: List<IssLocationData>) : ResponseState()
    data class Error(val message: String) : ResponseState()
}
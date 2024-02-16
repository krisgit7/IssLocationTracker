package com.example.isslocationtrackerapp.data.state

import com.example.isslocationtrackerapp.data.model.IssPassengerData

sealed class ResponsePassengerDataState {

    data class Success(val issPassengerData: IssPassengerData) : ResponsePassengerDataState()
    data class Error(val message: String) : ResponsePassengerDataState()
}
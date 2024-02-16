package com.example.isslocationtrackerapp.data.state

import com.example.isslocationtrackerapp.data.model.IssLocationData

sealed class ResponseState {

    object Loading : ResponseState()

    data class Success(val issLocations: List<IssLocationData>) : ResponseState()
    data class Error(val message: String) : ResponseState()
}
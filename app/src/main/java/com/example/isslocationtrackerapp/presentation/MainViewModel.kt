package com.example.isslocationtrackerapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.isslocationtrackerapp.data.ResponseState
import com.example.isslocationtrackerapp.data.repository.IssDataRepository
import com.example.isslocationtrackerapp.util.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion

class MainViewModel(
    private val issDataRepository: IssDataRepository
) : ViewModel() {

    val currentIssLocationLiveData: LiveData<ResponseState> = issDataRepository.getCurrentLocation()
        .map { issLocations ->
            issDataRepository.saveCurrentLocation(issLocations.first())
            ResponseState.SuccessLocation(issLocations) as ResponseState
        }
        .onCompletion {
            Log.d(Constants.TAG, "getCurrentIssLocationAsLiveData: onCompletion")
        }
        .catch { throwable ->
            emit(ResponseState.Error(throwable.message ?: "Unknown Error") as ResponseState)
        }
        .asLiveData()

    val issLocationsFromDB: LiveData<ResponseState> = issDataRepository.getLocationsFromDb()
        .map { issLocations ->
            ResponseState.SuccessLocation(issLocations) as ResponseState
        }
        .onCompletion {
            Log.d(Constants.TAG, "getIssLocationsFromDB: onCompletion")
        }
        .catch { throwable ->
            emit(ResponseState.Error(throwable.message ?: "Unknown Error") as ResponseState)
        }
        .asLiveData()
}
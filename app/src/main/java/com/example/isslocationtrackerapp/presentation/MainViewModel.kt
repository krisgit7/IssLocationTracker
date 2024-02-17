package com.example.isslocationtrackerapp.presentation

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.isslocationtrackerapp.data.location.LocationTracker
import com.example.isslocationtrackerapp.data.repository.IssDataRepository
import com.example.isslocationtrackerapp.data.state.ResponsePassengerDataState
import com.example.isslocationtrackerapp.data.state.ResponseState
import com.example.isslocationtrackerapp.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    private val issDataRepository: IssDataRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    val currentIssLocationLiveData: LiveData<ResponseState> = issDataRepository.getCurrentLocation()
        .map { issLocations ->
            issDataRepository.saveCurrentLocation(issLocations.first())
            ResponseState.Success(issLocations) as ResponseState
        }
        .onStart {
            emit(ResponseState.Loading)
        }
        .onCompletion {
            Log.d(Constants.TAG, "getCurrentIssLocationAsLiveData: onCompletion")
        }
        .retryWhen { cause, attempt ->
            val shouldRetry = cause is HttpException

            emit(ResponseState.Error(cause.message ?: "Unknown Error") as ResponseState)
            if (shouldRetry) {
                delay(5_000)
            }

            Log.d(Constants.TAG, "getCurrentIssLocationAsLiveData error cause: $cause, shouldRetry: $shouldRetry")
            shouldRetry
        }
        .asLiveData()

    val issLocationsFromDBLiveData: LiveData<ResponseState> = issDataRepository.getLocationsFromDb()
        .map { issLocations ->
            ResponseState.Success(issLocations) as ResponseState
        }
        .onCompletion {
            Log.d(Constants.TAG, "getIssLocationsFromDB: onCompletion")
        }
        .catch { throwable ->
            emit(ResponseState.Error(throwable.message ?: "Unknown Error") as ResponseState)
        }
        .asLiveData()

    val issPassengerLiveDataFlow: LiveData<ResponsePassengerDataState> = issDataRepository.getPassengerFlow(interval = 5_000L)
        .map { issPassengerData ->
            ResponsePassengerDataState.Success(issPassengerData) as ResponsePassengerDataState
        }
        .onCompletion {
            Log.d(Constants.TAG, "getPassengerFlow: onCompletion")
        }
        .retryWhen { cause, attempt ->
            Log.d(Constants.TAG, "getPassengerFlow error cause: $cause")
            delay(5_000)
            true
        }
        .asLiveData()

    private val _deviceLocation: MutableLiveData<Location> = MutableLiveData<Location>()
    val deviceLocation: LiveData<Location> = _deviceLocation

    fun getDeviceLocation() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                _deviceLocation.value = location
            }
        }
    }
}
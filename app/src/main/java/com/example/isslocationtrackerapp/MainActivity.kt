package com.example.isslocationtrackerapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.isslocationtrackerapp.data.ResponseState
import com.example.isslocationtrackerapp.databinding.ActivityMainBinding
import com.example.isslocationtrackerapp.presentation.MainViewModel
import com.example.isslocationtrackerapp.presentation.MainViewModelFactory
import com.example.isslocationtrackerapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this, mainViewModelFactory)
            .get(MainViewModel::class.java)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            Log.d(Constants.TAG, "onCreate: $it")
            startQueryingIssData()
        }

        if (hasLocationPermission()) {
            startQueryingIssData()
        } else {
            requestLocationPermission()
        }
    }

    private fun startQueryingIssData() {
        startGetCurrentIssLocation()
        startGetIssLocationsFromDB()
    }

    private fun startGetCurrentIssLocation() {
        viewModel.currentIssLocationLiveData.observe(this, {
            Log.d(Constants.TAG, it.toString())
            if (it is ResponseState.SuccessLocation) {
                val issLocationData = it.issLocations.first()
                val displayedText = "Current ISS Location: [Lat: ${issLocationData.issPosition.latitude}, Long: ${issLocationData.issPosition.longitude}]"
                binding.issLocationText.text = displayedText
            }
        })
    }

    private fun startGetIssLocationsFromDB() {
        viewModel.issLocationsFromDBEveryFiveSeconds.observe(this, {
            Log.d(Constants.TAG, it.toString())
            if (it is ResponseState.SuccessLocation) {
                val issLocationData = it.issLocations.last()
                val displayedText = "Current ISS Location DB: [Lat: ${issLocationData.issPosition.latitude}, Long: ${issLocationData.issPosition.longitude}]"
                binding.issLocationDbText.text = displayedText
            }
        })
    }

    private fun hasLocationPermission(): Boolean {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.d(Constants.TAG, "[hasLocationPermission]: " +
                "hasAccessFineLocationPermission: $hasAccessFineLocationPermission, " +
                "hasAccessCoarseLocationPermission: $hasAccessCoarseLocationPermission")
        return hasAccessFineLocationPermission || hasAccessCoarseLocationPermission
    }

    private fun requestLocationPermission() {
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ))
    }
}
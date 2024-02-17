package com.example.isslocationtrackerapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.isslocationtrackerapp.data.model.IssPassengerData
import com.example.isslocationtrackerapp.data.state.ResponseState
import com.example.isslocationtrackerapp.databinding.ActivityMainBinding
import com.example.isslocationtrackerapp.presentation.IssLocationAdapter
import com.example.isslocationtrackerapp.presentation.MainViewModel
import com.example.isslocationtrackerapp.presentation.MainViewModelFactory
import com.example.isslocationtrackerapp.util.Constants
import com.example.isslocationtrackerapp.util.FormatUtils
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val issLocationAdapter = IssLocationAdapter()

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    private var deviceLocation: Location? = null
    private var issPassengerData: IssPassengerData? = null

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
        initRecyclerView()
    }

    private fun startQueryingIssData() {
        getDeviceLocation()
        getIssPassenger()
        startGetCurrentIssLocation()
        startGetIssLocationsFromDB()
    }

    private fun getDeviceLocation() {
        viewModel.deviceLocation.observe(this) {
            deviceLocation = it
        }
        viewModel.getDeviceLocation()
    }

    private fun getIssPassenger() {
        viewModel.issPassengerLiveData.observe(this) {
            issPassengerData = it
        }
        viewModel.getIssPassengerData()
    }

    private fun startGetCurrentIssLocation() {
        viewModel.currentIssLocationLiveData.observe(this) { responseState ->
            Log.d(Constants.TAG, responseState.toString())
            when (responseState) {
                is ResponseState.Loading -> {
                    binding.issProgressbar.visibility = View.VISIBLE
                    binding.issLocationText.visibility = View.GONE
                }

                is ResponseState.Success -> {
                    renderIssInfo(responseState)
                }

                is ResponseState.Error -> {
                    Toast.makeText(this, responseState.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun renderIssInfo(responseState: ResponseState.Success) {
        val issLocationData = responseState.issLocations.first()
        val displayedText = if (deviceLocation != null && issPassengerData != null) {
            val deviceLat = deviceLocation!!.latitude
            val deviceLong = deviceLocation!!.longitude
            val deviceLatLong = LatLng(deviceLat, deviceLong)

            val issLat = issLocationData.issPosition.latitude.toDouble()
            val issLong = issLocationData.issPosition.longitude.toDouble()
            val issLatLong = LatLng(issLat, issLong)
            val distance = SphericalUtil.computeDistanceBetween(deviceLatLong, issLatLong)

            val deviceLatStr = FormatUtils.getFormatted(deviceLat)
            val deviceLongStr = FormatUtils.getFormatted(deviceLong)

            val issLatStr = FormatUtils.getFormatted(issLat)
            val issLongStr = FormatUtils.getFormatted(issLong)

            val distanceInKm = distance / 1000
            val distanceStr = FormatUtils.getFormatted(distanceInKm)

            val passengerStr = getPassengers(issPassengerData!!)
            "Device Location: [Lat: $deviceLatStr, Long: $deviceLongStr]\n" +
                    "Current ISS Location: [Lat: $issLatStr, Long: $issLongStr]\n" +
                    "Distance: $distanceStr Km\n" +
                    "Passenger: $passengerStr"
        } else {
            "Current ISS Location: [Lat: ${issLocationData.issPosition.latitude}, Long: ${issLocationData.issPosition.longitude}]"
        }
        binding.issLocationText.text = displayedText
        binding.issProgressbar.visibility = View.GONE
        binding.issLocationText.visibility = View.VISIBLE
    }

    private fun getPassengers(issPassenger: IssPassengerData): String {
        val peopleList = issPassenger.people.joinToString(", ") { people ->
            people.name
        }

        return peopleList
    }

    private fun startGetIssLocationsFromDB() {
        viewModel.issLocationsFromDBLiveData.observe(this) {
            Log.d(Constants.TAG, it.toString())
            if (it is ResponseState.Success) {
                val issLocations = it.issLocations
                issLocationAdapter.setList(issLocations)
                issLocationAdapter.notifyDataSetChanged()
            }
        }
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

    private fun initRecyclerView() {
        binding.issLocationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = issLocationAdapter
        }
    }
}
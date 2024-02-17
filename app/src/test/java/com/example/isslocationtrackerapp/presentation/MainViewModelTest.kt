package com.example.isslocationtrackerapp.presentation

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.isslocationtrackerapp.data.location.LocationTracker
import com.example.isslocationtrackerapp.data.model.IssLocationData
import com.example.isslocationtrackerapp.data.model.LocationData
import com.example.isslocationtrackerapp.data.repository.IssDataRepository
import com.example.isslocationtrackerapp.data.state.ResponseState
import com.example.isslocationtrackerapp.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    lateinit var mainViewModel: MainViewModel

    private val issDataRepository = mockk<IssDataRepository>(relaxed = true)
    private val locationTracker = mockk<LocationTracker>(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        mainViewModel = MainViewModel(issDataRepository, locationTracker)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun currentIssLocationLiveData_LiveDataChanged_GetLoading() = mainCoroutineRule.runTest {
        var savedText = ""
        val issLocationData = listOf(
            IssLocationData(
                id = 1,
                message = "success",
                issPosition = LocationData(latitude = "48.7749", longitude = "-135.2732"),
                timestamp = 1708113844
            )
        )
        coEvery {
            issDataRepository.saveCurrentLocation(any())
        } coAnswers {
            savedText = "Saved"
        }

        coEvery {
            issDataRepository.getCurrentLocation()
        } coAnswers {
            flow {
                emit(issLocationData)
            }
        }

        mainViewModel.currentIssLocationLiveData.observeForever { }

        assert(mainViewModel.currentIssLocationLiveData.value == ResponseState.Loading)
    }
}
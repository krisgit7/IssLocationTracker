package com.example.isslocationtrackerapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.isslocationtrackerapp.data.repository.IssDataRepository

class MainViewModelFactory(
    private val issDataRepository: IssDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IssDataRepository::class.java)
            .newInstance(issDataRepository)
    }
}
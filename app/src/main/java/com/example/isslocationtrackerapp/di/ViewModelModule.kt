package com.example.isslocationtrackerapp.di

import com.example.isslocationtrackerapp.data.repository.IssDataRepository
import com.example.isslocationtrackerapp.presentation.MainViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ViewModelModule {

    @ActivityScoped
    @Provides
    fun provideMainViewModelFactory(issDataRepository: IssDataRepository): MainViewModelFactory {
        return MainViewModelFactory(issDataRepository)
    }
}
package com.studio.eddy.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studio.eddy.myapplication.network.Repository
import javax.inject.Inject

class RealTimeMapViewModelFactory @Inject constructor(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RealTimeMapViewModel(repository) as T
    }
}
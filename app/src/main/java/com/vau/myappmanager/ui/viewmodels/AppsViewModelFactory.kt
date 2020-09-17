package com.vau.myappmanager.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vau.myappmanager.data.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppsViewModelFactory @Inject constructor(
    private val application: Application,
    private var AppRepository: AppRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppsViewModel.getInstance(application, AppRepository) as T
    }

    companion object {
        @Volatile private var instance : AppsViewModelFactory ?= null
        fun getInstance(application: Application,
                        appRepository : AppRepository) : AppsViewModelFactory
                = instance ?: synchronized(this) {
            instance ?:  AppsViewModelFactory(application,appRepository).also { instance = it }
        }
    }
}
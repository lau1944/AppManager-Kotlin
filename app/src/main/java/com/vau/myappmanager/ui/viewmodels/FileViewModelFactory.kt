package com.vau.myappmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vau.myappmanager.data.AppRepository
import javax.inject.Inject

class FileViewModelFactory @Inject constructor(
     private val appRepository: AppRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FileViewModel(appRepository) as T
    }
}
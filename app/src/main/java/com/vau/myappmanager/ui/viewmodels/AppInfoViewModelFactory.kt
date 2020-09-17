package com.vau.myappmanager.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vau.myappmanager.data.AppRepository
import javax.inject.Inject

class AppInfoViewModelFactory @Inject constructor(
    private var AppRepository: AppRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppInfoViewModel(AppRepository) as T
    }

}
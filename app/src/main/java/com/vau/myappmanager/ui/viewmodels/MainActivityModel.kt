package com.vau.myappmanager.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vau.myappmanager.objects.AppbarState

class MainActivityModel : ViewModel() {
    var stateMutable = MutableLiveData<AppbarState>()
    val state get() = stateMutable

    init {
        stateMutable.postValue(AppbarState.normal())
    }
}
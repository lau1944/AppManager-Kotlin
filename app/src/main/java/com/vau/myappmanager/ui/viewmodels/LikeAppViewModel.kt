package com.vau.myappmanager.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.vau.myappmanager.data.AppRepository
import com.vau.myappmanager.objects.LikedApp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LikeAppViewModel @Inject constructor (
    private var appRepository : AppRepository
): ViewModel() {
    private val LikedAppListLive = MutableLiveData<List<LikedApp>>()
    val LikedAppList : LiveData<List<LikedApp>> get() = LikedAppListLive


    fun getLikeApp() = viewModelScope.launch {
        appRepository.getAllLiked()
            .collect {
                Log.d("myAppCrash", it.size.toString())
                LikedAppListLive.value = it
            }
    }

}
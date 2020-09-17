package com.vau.myappmanager.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.myappmanager.data.AppRepository
import com.vau.myappmanager.objects.LikedApp
import com.vau.myappmanager.objects.Result
import com.vau.myappmanager.objects.EachApp
import com.vau.myappmanager.utils.DateHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.inject.Inject

class AppInfoViewModel @Inject constructor (
    private val appRepository: AppRepository
) : ViewModel(){
    private val appInfoLive = MutableLiveData<EachApp>()
    val appInfo : LiveData<EachApp> get() = appInfoLive

    private val isLikeLive = MutableLiveData<Boolean>()
    val isLike : LiveData<Boolean> get() = isLikeLive


    fun getAppInfo(appId: String) = viewModelScope.launch {
        appRepository.getAppInfo(appId).collect {
            when (it) {
                is Result.Success -> {
                    appInfoLive.postValue(it.data)
                }
                is Result.Error -> {
                    appInfoLive.postValue(null)
                }
            }
        }
    }


    fun isLike (packageName: String) = viewModelScope.launch {
        appRepository.isLiked(packageName).collect {
            isLikeLive.value = it > 0
        }
    }

    fun dislikeApp(likedApp: LikedApp) = viewModelScope.launch {
        appRepository.deleteLiked(likedApp)
    }

    fun likeApp (likedApp: LikedApp) = viewModelScope.launch {
        appRepository.insertLiked(likedApp)
    }

    fun transformToDate(time: String) : String {
        return DateHelper.convertLongToTime(time.toLong())
    }
    fun permissionBuilder(list: List<String>) : String {
        val builder = StringBuilder()
        for (item in list) {
            if (item.contains("NETWORK")
                || item.contains("LOCATION")
                || item.contains("SYSTEM")
                || item.contains("STORAGE")
                || item.contains("CAMERA"))
            builder.append(item + "\n")
        }
        builder.append("...")
        return builder.toString()
    }

}
package com.vau.myappmanager.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.vau.myappmanager.data.AppRepository
import com.vau.myappmanager.objects.EachApp
import kotlinx.coroutines.flow.*
import com.vau.myappmanager.objects.Result
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppsViewModel @Inject constructor (
    application: Application,
    private var appRepository : AppRepository
): AndroidViewModel(application) {

    private val applistLive = MutableLiveData<List<EachApp>>()
    private val SystemApplistLive = MutableLiveData<List<EachApp>>()

    var applist = MutableLiveData<List<EachApp>>()
    var SystemApplist = MutableLiveData<List<EachApp>>()

    private val applistLiveSize = MutableLiveData<String>()
    var applistSize : LiveData<String> = applistLiveSize

    init {
        getPackages()
    }

    fun getPackages() = CoroutineScope(Dispatchers.Main).launch {
        getAllPackage()
    }

    fun getAppList(): LiveData<List<EachApp>> {
        return applist
    }

    fun getSystemAppList(): LiveData<List<EachApp>> {
        return SystemApplist
    }

    fun getFilteredApps(text: String) {
        try {
            applist.value = applistLive.value!!.filter {
                it.name!!.toLowerCase().startsWith(text.toLowerCase())
                        || it.packageName!!.toLowerCase().startsWith(text.toLowerCase())
            }
            SystemApplist.value = SystemApplistLive.value!!.filter {
                it.name!!.toLowerCase().startsWith(text.toLowerCase())
                        || it.packageName!!.toLowerCase().startsWith(text.toLowerCase())
            }
        } catch (e: Exception) {
            Log.d("myAppCrash", e.toString())
        }
    }

    suspend fun getAllPackage() = withContext(Dispatchers.IO) {
            appRepository
                .getApps()
                .collect {
                    when (it) {
                        is Result.Success -> {
                            val listA = it.data.filter { it2 ->
                                !it2.isSystemApp
                            }
                            val listS = it.data.filter { it2 ->
                                it2.isSystemApp
                            }
                            withContext(Dispatchers.Main) {
                                applistLiveSize.value = listA.size.toString()
                                applistLive.value = listA
                                SystemApplistLive.value = listS
                                applist.value = listA
                                SystemApplist.value = listS
                            }
                        }
                        is Result.Error -> {

                        }
                    }
                }
    }

    companion object {
        @Volatile private var instance : AppsViewModel ?= null
        fun getInstance(application: Application,
                        appRepository : AppRepository) : AppsViewModel
                = instance ?: synchronized(this) {
            instance ?:  AppsViewModel(application, appRepository).also { instance = it }
        }
    }
}
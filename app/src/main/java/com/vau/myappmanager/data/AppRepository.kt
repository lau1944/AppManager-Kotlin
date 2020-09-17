package com.vau.myappmanager.data

import android.content.Context
import com.vau.myappmanager.objects.LikedApp
import com.vau.myappmanager.objects.Result
import com.vau.myappmanager.objects.EachApp
import com.vau.myappmanager.objects.ExtractApp
import com.vau.myappmanager.utils.SharedPreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepository constructor(
    private val context: Context
){
    @Inject private val dataService = DataService.getInstance(context)
    fun getApps() : Flow<Result<List<EachApp>>> {
        return dataService.getApps().flowOn(Dispatchers.IO)
    }

    fun getAppInfo (appId: String) : Flow<Result<EachApp>> {
        return dataService.getAppInfo(appId).flowOn(Dispatchers.Default)
    }

    fun getExtractApp() : Flow<List<ExtractApp>> {
        return dataService.getExtractApp().flowOn(Dispatchers.Default)
    }


    fun getAllLiked () : Flow<List<LikedApp>> {
        return dataService.getAllLikedApp().flowOn(Dispatchers.Default)
    }

    fun isLiked(packageName: String) : Flow<Int> {
        return dataService.isLike(packageName)
    }

    suspend fun insertLiked (likedApp: LikedApp) = withContext(Dispatchers.IO){
        dataService.insertLiked(likedApp)
    }

    suspend fun deleteLiked (likedApp: LikedApp) = withContext(Dispatchers.IO){
        dataService.deleteLiked(likedApp)
    }

    suspend fun insertExtract (ExtractApp: ExtractApp) = withContext(Dispatchers.IO){
        dataService.insertExtract(ExtractApp)
    }

    suspend fun deleteExtract (ExtractApp: ExtractApp) = withContext(Dispatchers.IO){
        dataService.deleteExtract(ExtractApp)
    }

    suspend fun deleteAllExtract() = withContext(Dispatchers.IO) {
        dataService.deleteAllExtract()
    }

    fun getExternalFile(contextAsActivity: Context) : String {
        return SharedPreferenceHelper.getFolderData(contextAsActivity)
    }

    companion object {
        @Volatile private var instance : AppRepository ?= null
        fun getInstance(context: Context) : AppRepository
                = instance ?: synchronized(this) {
            instance ?:  AppRepository(context).also { instance = it }
        }
    }
}
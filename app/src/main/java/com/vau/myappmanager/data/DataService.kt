package com.vau.myappmanager.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.vau.myappmanager.R
import com.vau.myappmanager.objects.LikedApp
import com.vau.myappmanager.objects.EachApp
import com.vau.myappmanager.objects.Result
import com.vau.myappmanager.objects.ExtractApp
import dagger.Component
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Singleton

@Singleton
@Component
class DataService (
    private val context: Context
){
    //get all package from files
    fun getApps() : Flow<Result<List<EachApp>>> = flow {
        try {
            val appList = ArrayList<EachApp>()
            val manager = context.applicationContext.packageManager
            val list = manager.getInstalledPackages(0) as ArrayList<PackageInfo>
            for (each_package in list) {
                val source = each_package.applicationInfo.sourceDir
                val file = File(source)
                val size = (file.length() / 1000000).toString() + "MB"
                val refList = ArrayList<String>()
                if (each_package.requestedPermissions == null) {
                    refList.add("")
                }
                appList.add(
                    EachApp(
                        each_package.applicationInfo.loadIcon(manager),
                        each_package.versionName,
                        each_package.applicationInfo.packageName!!,
                        each_package.applicationInfo.dataDir,
                        each_package.lastUpdateTime.toString(),
                        source!!,
                        each_package.applicationInfo.loadLabel(manager).toString(),
                        size,
                        refList,
                        isSystemPackage(each_package)
                    )
                )
            }
            emit(Result.Success(appList))
        } catch (e: Exception) {
            Log.d("myAppCrash", e.toString())
            emit(Result.Error(e))
        }
    }

    fun getAppInfo(appId: String) : Flow<Result<EachApp>> = flow {
        try {
            Log.d("myAppCrash", appId)
            val manager = context.applicationContext.packageManager
            val each_package = manager.getPackageInfo(appId, PackageManager.GET_PERMISSIONS)
            val source = each_package.applicationInfo.sourceDir
            val file = File(source)

            var refList = ArrayList<String>()
            if (each_package.requestedPermissions == null) {
                refList.add(context.resources.getString(R.string.no_permission))
            } else {
                refList = ArrayList(each_package.requestedPermissions.asList())
            }

            val size = (file.length() / 1000000).toString() + "MB"
            val appInfo = EachApp(
                each_package.applicationInfo.loadIcon(manager),
                each_package.versionName,
                each_package.applicationInfo.packageName!!,
                each_package.applicationInfo.dataDir,
                each_package.lastUpdateTime.toString(),
                source!!,
                each_package.applicationInfo.loadLabel(manager).toString(),
                size,
                refList,
                isSystemPackage(each_package)
            )
            Log.d("myAppCrash", refList[0])
            emit(Result.Success(appInfo))
        }catch (e: java.lang.Exception) {
            Log.d("myAppCrash", e.toString())
            emit(Result.Error(e))
        }
    }

    fun getAllLikedApp() : Flow<List<LikedApp>> {
        return AppDatabase.getInstance(context).LikedDao().getAll()
    }

    fun isLike(packageName: String) : Flow<Int> {
        return AppDatabase.getInstance(context).LikedDao().isLiked(packageName)
    }

    suspend fun insertLiked(LikedDao : LikedApp) {
        try {
            AppDatabase.getInstance(context).LikedDao().insert(LikedDao)
        } catch (e: java.lang.Exception) {}
    }

    suspend fun deleteLiked(LikedDao : LikedApp) {
        try {
            AppDatabase.getInstance(context).LikedDao().delete(LikedDao)
        }catch (e: java.lang.Exception) {}
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    fun getExtractApp() : Flow<List<ExtractApp>> {
        return AppDatabase.getInstance(context).extractAppDao().getAll()
    }

    suspend fun insertExtract(ExtractApp: ExtractApp) {
       try {
           AppDatabase.getInstance(context).extractAppDao().insert(ExtractApp)
       }catch (e: java.lang.Exception) {
           Log.d("myAppCrash", e.toString())
       }
    }

    suspend fun deleteExtract(ExtractApp: ExtractApp) {
        try {
            AppDatabase.getInstance(context).extractAppDao().delete(ExtractApp)
        }catch (e: java.lang.Exception) {}
    }

    suspend fun deleteAllExtract() {
        try {
            AppDatabase.getInstance(context).extractAppDao().deleteAll()
        } catch (e: java.lang.Exception) {

        }
    }

    companion object {
        @Volatile private var instance : DataService ?= null
        fun getInstance(context: Context) : DataService
                = instance ?: synchronized(this) {
            instance ?:  DataService(context).also { instance = it }
        }
    }
}
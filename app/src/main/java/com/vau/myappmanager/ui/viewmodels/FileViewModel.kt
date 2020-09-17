package com.vau.myappmanager.ui.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vau.myappmanager.BuildConfig
import com.vau.myappmanager.R
import com.vau.myappmanager.data.AppRepository
import com.vau.myappmanager.objects.ExtractApp
import com.vau.myappmanager.utils.SharedPreferenceHelper
import com.vau.myappmanager.utils.CopyFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    private val MutableUri = MutableLiveData<Uri>()
    val uriData : LiveData<Uri> get() = MutableUri

    private val MutableExtractApp = MutableLiveData<List<ExtractApp>>()
    val extractApp : LiveData<List<ExtractApp>> get() = MutableExtractApp

    private val MutableExtractAppSize = MutableLiveData<String>()
    val extractAppSize : LiveData<String> get() = MutableExtractAppSize

    fun deleteFile(dir: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val file = File(dir)
            file.delete()
        }
    }

    fun getExtractApp() = viewModelScope.launch {
        appRepository.getExtractApp().collect {
            MutableExtractApp.value = it
            MutableExtractAppSize.value = it.size.toString()
        }
    }

    fun insertExtract(ExtractApp: ExtractApp) = viewModelScope.launch {
        appRepository.insertExtract(ExtractApp)
    }

    fun deleteExtract(ExtractApp: ExtractApp) = viewModelScope.launch {
        appRepository.deleteExtract(ExtractApp)
    }

    fun updateExtractedList(ExtractApp: List<ExtractApp>) {
        MutableExtractApp.value = ExtractApp
    }


    fun clearCache (context: Context) = viewModelScope.launch {
        clearCacheFile(context)
    }

    suspend fun clearCacheFile(context: Context) = withContext(Dispatchers.IO) {
        val file = context.cacheDir
        if (file.exists() && file.isDirectory) {
            for (item in file.list()!!) {
                val fileDelete = File(file, item)
                fileDelete.delete()
            }
            file.delete()
        }
    }

    fun getLocation(context: Context) : String {
        return Environment.getExternalStorageDirectory().toString() + "/" + appRepository.getExternalFile(context)
    }

    fun setFolderLocation(context: Context, location: String) = viewModelScope.launch {
        AppRepository.getInstance(context).deleteAllExtract()
        SharedPreferenceHelper.setFolderData(context, location.trim())
    }

    fun getSource (source: String, label: String, isShare: Boolean,
                   ExtractApp: ExtractApp, context: Context) = viewModelScope.launch {
        extractApk(source, label, isShare, ExtractApp, context)
    }

    suspend fun extractApk(source: String, label: String,
                           isShare: Boolean, ExtractApp: ExtractApp, context: Context)
            = withContext(Dispatchers.IO) {
        val origin = File (source)
        try {
            var tempFile = CopyFile.getAbsoluteDir(context, appRepository)
            Log.d("myAppCrash", tempFile.toString())

            if (!tempFile.exists()) {
                if (!tempFile.mkdirs()) {}
            }

            tempFile =
                File(tempFile.path + "/" + label.replace(" ", "").toLowerCase() + ".apk")

            if (!tempFile.exists()) {
                tempFile.createNewFile()
            }

            //origin.copyTo(tempFile)

            //Log.d("myAppCrash", tempFile.toString())


            val out = FileOutputStream(tempFile)
            val input = FileInputStream(origin)

            val byteBuffer = ByteArray(1024)
            var len: Int
            while (input.read(byteBuffer).also { len= it } > 0) {
                out.write(byteBuffer, 0, len)
            }

            insertExtract(ExtractApp(ExtractApp.packageName,
                ExtractApp.size, ExtractApp.name, tempFile.toString()))

            //input.close()
           // out.close()

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, context.resources.getString(R.string.file_extract_location) +
                            tempFile.path, Toast.LENGTH_LONG
                ).show()
            }

            if (isShare) {
                shareApk(
                    FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".provider", tempFile
                    )
                )
            } else {}
        } catch (e: Exception) {
            Log.d("myAppCrash", e.toString())
        }
    }

    private fun shareApk(uri: Uri) {
        CoroutineScope(Dispatchers.Main).launch {
            MutableUri.value = uri
        }
    }

    fun getIcon(context: Context, packageName: String) : Drawable{
        return context.packageManager.getPackageInfo(packageName, 0).applicationInfo.loadIcon(context.packageManager)
    }

    companion object {
        @Volatile private var instance : FileViewModel ?= null

        fun getInstance(appRepository: AppRepository) : FileViewModel
                = instance ?: synchronized(this) {
            instance ?:  FileViewModel(appRepository).also { instance = it }
        }
    }
}
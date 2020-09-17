package com.vau.myappmanager.utils

import android.app.Application
import android.content.Context
import com.vau.myappmanager.data.AppRepository
import com.vau.myappmanager.ui.viewmodels.*
import dagger.Provides
import javax.inject.Singleton

object InjectUtil {
    @Provides
    @Singleton
    fun provideAppsRepository(context: Context) : AppRepository {
        return AppRepository.getInstance(context)
    }
    @Provides
    @Singleton
    fun provideAppsViewModelFactory(application: Application) : AppsViewModelFactory {
        return AppsViewModelFactory.getInstance(application, provideAppsRepository(application.applicationContext))
    }
    @Provides
    @Singleton
    fun provideAppInfoViewModelFactory(context: Context) : AppInfoViewModelFactory {
        return AppInfoViewModelFactory(provideAppsRepository(context))
    }
    @Provides
    @Singleton
    fun provideLikeAppViewModelFactory(context: Context) : LikeAppVieModelFactory {
        return LikeAppVieModelFactory(provideAppsRepository(context))
    }
    @Provides
    @Singleton
    fun provideFileViewModelFactory(context: Context) : FileViewModelFactory {
        return FileViewModelFactory(provideAppsRepository(context))
    }
}
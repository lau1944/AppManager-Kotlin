package com.vau.myappmanager.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object SharedPreferenceHelper {
    fun setFolderData(context: Context, fileDir: String) : Boolean{
        val sharedPreferences = (context as Activity).getSharedPreferences("Folder", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("fileDir", fileDir)
        editor.apply()
        return true
    }

    fun getFolderData(context: Context) : String{
        val sharedPreferences = (context as Activity).getSharedPreferences("Folder", Context.MODE_PRIVATE)
        return sharedPreferences.getString("fileDir", "MyAppManagerPro")!!
    }

    fun setDarkSetting(context: Context, isCheck: Boolean) {
        val pref = context.getSharedPreferences("Mode", 0)
        val editor = pref.edit()
        if (isCheck) {
            editor.putInt("Mode", AppCompatDelegate.MODE_NIGHT_YES)
            editor.apply()
        } else {
            editor.putInt("Mode", AppCompatDelegate.MODE_NIGHT_NO)
            editor.apply()
        }
    }

    fun getDarkSetting (context: Context) : Int {
        val pref = context.getSharedPreferences("Mode", 0)
        return pref.getInt("Mode", 0)
    }
}
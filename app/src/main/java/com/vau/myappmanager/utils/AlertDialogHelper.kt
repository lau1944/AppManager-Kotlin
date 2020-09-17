package com.vau.myappmanager.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import javax.inject.Singleton

@Singleton
class AlertDialogHelper{
    fun buildDialog(context : Context,
                    title: String,
                    message: String
    ) : AlertDialog.Builder {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
            .setMessage(message)

        return dialog
    }


    companion object {
        private var instance : AlertDialogHelper ?= null
        fun getInstance() : AlertDialogHelper
            = instance ?: synchronized(this) {
                instance ?: AlertDialogHelper()
                    .also { instance = it }
        }

    }
}
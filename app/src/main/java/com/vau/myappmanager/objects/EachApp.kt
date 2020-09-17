package com.vau.myappmanager.objects

import android.graphics.drawable.Drawable

data class EachApp (val icon : Drawable,
                    val version : String ?= "",
                    val packageName : String ?= "",
                    val dataDir: String ?= "",
                    val lastUpdate: String ?= "",
                    val source : String ?= "",
                    val name : String ?= "",
                    val size: String ?= "",
                    var permission: List<String> ?= null,
                    val isSystemApp: Boolean){
}
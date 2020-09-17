package com.vau.myappmanager.objects

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikedApp (
    @PrimaryKey val packageName : String,
    @ColumnInfo(name = "size") val size : String ?= "",
    @ColumnInfo(name = "name") val name : String ?= "",
    @ColumnInfo(name = "source") val source : String ?= ""
){

}
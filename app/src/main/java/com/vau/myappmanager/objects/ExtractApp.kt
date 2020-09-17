package com.vau.myappmanager.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExtractApp (
    @PrimaryKey val packageName : String,
    @ColumnInfo(name = "size") val size : String ?= "",
    @ColumnInfo(name = "name") val name : String ?= "",
    @ColumnInfo(name = "dir") var dir : String ?= ""
){

}
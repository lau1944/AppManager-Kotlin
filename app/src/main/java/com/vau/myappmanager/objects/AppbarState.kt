package com.vau.myappmanager.objects

sealed class AppbarState {
    class search() : AppbarState()
    class normal() : AppbarState()
}
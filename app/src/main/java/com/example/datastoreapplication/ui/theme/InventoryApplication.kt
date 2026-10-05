package com.example.datastoreapplication.ui.theme

import android.app.Application

class InventoryApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
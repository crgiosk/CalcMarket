package com.calcmarket

import android.app.Application
import com.calcmarket.core.PreferencesHelper
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesHelper(this).migrateOldPreferences()
        FirebaseApp.initializeApp(this)
    }
}
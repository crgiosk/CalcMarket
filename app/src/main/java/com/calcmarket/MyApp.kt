package com.calcmarket

import android.app.Application
import com.calcmarket.core.PreferencesHelper
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesHelper(this).migrateOldPreferences()
    }
}
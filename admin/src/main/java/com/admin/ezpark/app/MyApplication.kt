package com.admin.ezpark.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.admin.ezpark.data.local.sharedpreference.SharedPreferencesManager
import com.admin.ezpark.utils.IS_DARK_MODE
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate() {
        super.onCreate()

        themeSelector()
    }

    private fun themeSelector() {
        val isDarkMode = sharedPreferencesManager.getBoolean(IS_DARK_MODE)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
package com.admin.ezpark.app

import android.app.Application
import com.admin.ezpark.data.local.sharedpreference.SharedPreferencesManager
import com.admin.ezpark.utils.IS_DARK_MODE
import com.admin.ezpark.utils.Utils.mode
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate() {
        super.onCreate()

        checkThemeMode()
    }

    private fun checkThemeMode() = mode(sharedPreferencesManager.getBoolean(IS_DARK_MODE))

}
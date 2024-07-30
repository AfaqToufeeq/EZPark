package com.admin.ezpark.data.local.sharedpreference

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()

    fun getString(key: String, defaultValue: String = ""): String =
        sharedPreferences.getString(key, defaultValue).orEmpty()

    fun saveInt(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).apply()

    fun getInt(key: String, defaultValue: Int = 0): Int =
        sharedPreferences.getInt(key, defaultValue)

    fun saveLong(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).apply()

    fun getLong(key: String, defaultValue: Long = 0L): Long =
        sharedPreferences.getLong(key, defaultValue)

    fun saveFloat(key: String, value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

    fun getFloat(key: String, defaultValue: Float = 0.0f): Float =
        sharedPreferences.getFloat(key, defaultValue)

    fun saveBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    fun remove(key: String) = sharedPreferences.edit().remove(key).apply()

    fun clear() = sharedPreferences.edit().clear().apply()
}

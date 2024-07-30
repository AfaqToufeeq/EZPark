package com.admin.ezpark.di

import android.content.Context
import android.content.SharedPreferences
import com.admin.ezpark.utils.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProvider()
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    }
}
package com.admin.ezpark.data.local.repository

import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.utils.DataProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor() {
    suspend fun getDashboardCards(): List<DashboardCard> {
        return DataProvider.getDashboardCards()
    }
}

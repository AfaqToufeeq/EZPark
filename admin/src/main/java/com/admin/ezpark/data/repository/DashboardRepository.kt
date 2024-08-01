package com.admin.ezpark.data.repository

import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.utils.DataProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor() {

    suspend fun getDashboardCardsType1(): List<DashboardCard> = DataProvider.getDashboardCardsType1()

    suspend fun getDashboardCardsType2(): List<DashboardCard> = DataProvider.getDashboardCardsType2()
}

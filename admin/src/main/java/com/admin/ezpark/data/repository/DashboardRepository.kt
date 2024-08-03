package com.admin.ezpark.data.repository

import com.admin.ezpark.data.models.DashboardCompModel
import com.admin.ezpark.utils.DataProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor() {

    suspend fun getDashboardCardsType1(): DashboardCompModel = DataProvider.getDashboardCardsType1()

    suspend fun getDashboardCardsType2(): DashboardCompModel = DataProvider.getDashboardCardsType2()

    suspend fun getDashboardCardsType3(): DashboardCompModel = DataProvider.getDashboardCardsType3()
}

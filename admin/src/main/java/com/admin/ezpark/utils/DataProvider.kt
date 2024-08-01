package com.admin.ezpark.utils

import com.admin.ezpark.R
import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.enums.DashboardFields
import com.admin.ezpark.enums.DashboardType

object DataProvider {

    suspend fun getDashboardCardsType1(): List<DashboardCard> {
        return listOf(
            DashboardCard(number = 100, heading = "ParkingLots", type =  DashboardType.TYPE1.type),
            DashboardCard(number = 50, heading = "Owners", type =  DashboardType.TYPE1.type),
        )
    }

    suspend fun getDashboardCardsType2(): List<DashboardCard> {
        return listOf(
            DashboardCard(title = DashboardFields.AddOwner, imageResource = R.drawable.location_icon, type = DashboardType.TYPE2.type),
            DashboardCard(title = DashboardFields.ViewOwner, imageResource = R.drawable.dashboard_icon, type = DashboardType.TYPE2.type),
            DashboardCard(title = DashboardFields.RemoveOwner, imageResource = R.drawable.location_icon, type = DashboardType.TYPE2.type),
        )
    }
}

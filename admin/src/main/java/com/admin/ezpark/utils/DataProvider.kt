package com.admin.ezpark.utils

import com.admin.ezpark.R
import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.data.models.DashboardCompModel
import com.admin.ezpark.enums.DashboardFields
import com.admin.ezpark.enums.DashboardType

object DataProvider {

    suspend fun getDashboardCardsType1(): DashboardCompModel{
        return DashboardCompModel(
            listOf(
                Pair("First Cards", listOf(
                    DashboardCard(number = 100, heading = "Parking Lots", type =  DashboardType.TYPE1.type),
                    DashboardCard(number = 50, heading = "Owners", type =  DashboardType.TYPE1.type),
                )
                )
            )
        )
    }

    suspend fun getDashboardCardsType2(): DashboardCompModel {
        return DashboardCompModel(
            listOf(
                Pair("Manage Owners", listOf(
                    DashboardCard(title = DashboardFields.AddOwner, imageResource = R.drawable.add_person_ic, type = DashboardType.TYPE2.type),
                    DashboardCard(title = DashboardFields.ViewOwner, imageResource = R.drawable.owners_ic, type = DashboardType.TYPE2.type),
                    DashboardCard(title = DashboardFields.UpdateOwnerInfo, imageResource = R.drawable.update_person_ic, type = DashboardType.TYPE2.type),
                    DashboardCard(title = DashboardFields.RemoveOwner, imageResource = R.drawable.remove_person_ic, type = DashboardType.TYPE2.type),
                )),
                Pair("Manage Parking Lots", listOf(
                    DashboardCard(title = DashboardFields.AddParkingLot, imageResource = R.drawable.add_parking_ic, type = DashboardType.TYPE2.type),
                    DashboardCard(title = DashboardFields.ViewParkingLot, imageResource = R.drawable.view_parking_ic, type = DashboardType.TYPE2.type),
                    DashboardCard(title = DashboardFields.UpdateParkingLot, imageResource = R.drawable.update_parking_ic, type = DashboardType.TYPE2.type),
                    DashboardCard(title = DashboardFields.RemoveParkingLot, imageResource = R.drawable.remove_parking_ic, type = DashboardType.TYPE2.type),
                ))
            )
        )
    }


    suspend fun getDashboardCardsType3(): DashboardCompModel {
        return DashboardCompModel(
            listOf(
                Pair("Second Cards", listOf(
                    DashboardCard(heading = "Chase Up Parking", imageResource = R.drawable.garage_sample, hours = "12-hours", type =  DashboardType.TYPE3.type),
                    DashboardCard(heading = "Al-fateh Parking", imageResource = R.drawable.garage_sample2, hours = "24-hours", type =  DashboardType.TYPE3.type),
                    DashboardCard(heading = "Giga Mall Parking", imageResource = R.drawable.garage_sample3, hours = "24-hours", type =  DashboardType.TYPE3.type),
                )
                )
            )
        )
    }

}

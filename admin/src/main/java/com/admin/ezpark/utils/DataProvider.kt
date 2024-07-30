package com.admin.ezpark.utils

import com.admin.ezpark.data.models.DashboardCard

object DataProvider {

    suspend fun getDashboardCards(): List<DashboardCard> {
        return listOf(
            DashboardCard(
                title = "Add Parking Station",
                content = "Add a new parking station with complete details.",
                actionText = "Add Station"
            ),
            DashboardCard(
                title = "View Parking Stations",
                content = "View and manage all parking stations.",
                actionText = "View Stations"
            ),
            DashboardCard(
                title = "Update Parking Station",
                content = "Update the details of existing parking stations.",
                actionText = "Update Station"
            ),
            DashboardCard(
                title = "Delete Parking Station",
                content = "Remove parking stations from the system.",
                actionText = "Delete Station"
            ),
            DashboardCard(
                title = "Manage Owners",
                content = "View and manage details of parking station owners.",
                actionText = "Manage Owners"
            ),
            DashboardCard(
                title = "Set Pricing",
                content = "Set and manage the pricing for parking slots.",
                actionText = "Set Pricing"
            ),
            DashboardCard(
                title = "View Revenue",
                content = "View the revenue generated from parking fees.",
                actionText = "View Revenue"
            )
        )
    }
}

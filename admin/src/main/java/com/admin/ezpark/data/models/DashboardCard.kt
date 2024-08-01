package com.admin.ezpark.data.models

import com.admin.ezpark.enums.DashboardFields


data class DashboardCard(
    val number: Int? = null,             // For type 1, represents the number of parking lots
    val heading: String? = null,         // For type 1, represents the heading

    val title: DashboardFields? = null,  // For type 2, represents the card title
    val content: String? = null,         // For type 2, represents the card content
    val imageResource: Int? = null,      // For type 2, represents the image resource ID
    val type: Int                        // Type of the card (1 or 2)
)
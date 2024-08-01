package com.admin.ezpark.sealedclass

import com.admin.ezpark.enums.DashboardFields

sealed class DashboardNavigationAction {
    object NavigateToAddOwner : DashboardNavigationAction()
    object NavigateToViewOwner : DashboardNavigationAction()
}

// Base interface for dashboard card items
sealed class DashboardCardItem {
    data class Type1(val number: Int, val heading: String) : DashboardCardItem()
    data class Type2(val title: DashboardFields, val imageResource: Int) : DashboardCardItem()
}

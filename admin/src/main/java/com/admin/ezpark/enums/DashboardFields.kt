package com.admin.ezpark.enums

enum class DashboardFields {
    AddOwner,
    RemoveOwner,
    UpdateOwnerInfo,
    ViewOwner,

    AddParkingLot,
    RemoveParkingLot,
    UpdateParkingLot,
    ViewParkingLot;


    fun toFormattedString(): String {
        // Split the enum name by uppercase letters and join with a space
        return name.split("(?=[A-Z])".toRegex()).joinToString(" ")
    }
}

enum class DashboardType(val type: Int) {
    TYPE1(1),
    TYPE2(2),
    TYPE3(3)
}
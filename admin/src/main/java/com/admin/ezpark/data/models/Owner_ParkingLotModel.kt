package com.admin.ezpark.data.models

import com.admin.ezpark.callbacks.Identifiable
import com.admin.ezpark.enums.Gender


data class OwnerModel(
    override var id: String? = null,
    val ownerName: String = "",
    val gender: Gender = Gender.OTHER,
    val dateOfBirth: String = "",
    val cnicOrPassport: String = "",
    var profileImageUrl: String? = null,
    val email: String = "",
    val phone: String = "",
    val address: String = "",val parkingStationIds: List<String> = emptyList()
) : Identifiable


data class ParkingLotModel(
    val stationId: String = "",
    val stationName: String = "",
    val stationImageUrl: String? = null,
    val stationAddress: String = "",
    val numberOfParkingSpaces: Int = 0,
    val availableSpaces: Int = 0,
    val operatingHours: String = "",
    val amenities: List<String> =emptyList(),
    val accessibility: List<String> = emptyList(),
    val hourlyRate: Double = 0.0,
    val dailyRate: Double = 0.0,
    val monthlyPass: Double? = null,
    val contactNumber: String = "",
    val emailAddress: String = "",
    val customerSupport: String? = null,
    val averageRating: Float = 0.0f,
    val reviews: List<String> = emptyList(),
    val bookingOptions: String? =null,
    val paymentMethods: List<String> = emptyList()
)
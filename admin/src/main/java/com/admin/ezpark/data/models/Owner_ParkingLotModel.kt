package com.admin.ezpark.data.models

import com.admin.ezpark.enums.Gender


data class OwnerModel(
    val ownerId: String,       // Unique identifier for the owner (Firestore document ID)
    val ownerName: String,
    val gender: Gender,
    val dateOfBirth: String,
    val cnicOrPassport: String,
    val profileImageUrl: String? = null,

    // Contact Information
    val email: String,
    val phone: String,
    val address: String,

    // List of parking stations owned (This can also be managed as a separate collection)
    val parkingStationIds: List<String> = emptyList() // List of ParkingStation document IDs
)

data class ParkingLotModel(
    val stationId: String,              // Unique identifier for the station
    val stationName: String,            // Name of the parking station
    val stationImageUrl: String? = null, // URL to an image of the parking station
    val stationAddress: String,         // Address of the parking station
    val numberOfParkingSpaces: Int,     // Total number of parking spaces
    val availableSpaces: Int,           // Currently available parking spaces
    val operatingHours: String,         // Operating hours (e.g., 24/7 or specific times)
    val amenities: List<String> = emptyList(),  // List of amenities (e.g., EV charging, security cameras)
    val accessibility: List<String> = emptyList(), // Accessibility features (e.g., reserved spots)
    val hourlyRate: Double,             // Hourly rate for parking
    val dailyRate: Double,              // Daily rate for parking
    val monthlyPass: Double? = null,    // Monthly pass rate (if available)
    val contactNumber: String,          // Contact phone number
    val emailAddress: String,           // Contact email address
    val customerSupport: String? = null, // Additional customer support information
    val averageRating: Float = 0.0f,    // Average user rating
    val reviews: List<String> = emptyList(), // List of user reviews
    val bookingOptions: String? = null, // Information on booking options
    val paymentMethods: List<String> = emptyList() // Accepted payment methods
)

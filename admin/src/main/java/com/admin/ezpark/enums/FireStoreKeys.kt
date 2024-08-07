package com.admin.ezpark.enums

enum class FireStoreKeys {

    owners;

    fun toFormattedString(): String {
        // Split the enum name by uppercase letters and join with a space
        return name.split("(?=[A-Z])".toRegex()).joinToString(" ")
    }
}
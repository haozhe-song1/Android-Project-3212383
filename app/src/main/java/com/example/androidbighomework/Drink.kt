package com.example.androidbighomework

// Both preset drinks and (later) user-added drinks are shown with this object.
data class Drink(
    val id: Int,
    val name: String,
    val calories: Double,
    val sugar: Double,
    val caffeine: Double,
    val description: String,
    val steps: String
)

package com.openclassrooms.joiefull.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Clothe(
    val id: Int,
    val picture: String,
    val name: String,
    val category: String,
    val likes: Int,
    val priceInCents: Int,
    val originalPriceInCents: Int,
)

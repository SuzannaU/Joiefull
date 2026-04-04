package com.openclassrooms.joiefull.data.entity

import com.openclassrooms.joiefull.domain.model.Category
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Clothe(
    val id: Long,
    val picture: Picture,
    val name: String,
    val category: Category,
    val likes: Long,
    val price: Double,
    @Json(name = "original_price")
    val originalPrice: Double,
)

@JsonClass(generateAdapter = true)
data class Picture(
    val url: String,
    val description: String,
)

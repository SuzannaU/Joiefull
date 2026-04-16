package com.openclassrooms.joiefull.domain.model

data class ProductDto(
    val id: Long,
    val name: String,
    val category: Category,
    val likes: Long,
    val rating: Int =0,
    val review: String = "",
    val isLiked: Boolean = false,
    val pictureUrl: String,
    val pictureDescription: String,
    val priceInCents: Long,
    val originalPriceInCents: Long,
)
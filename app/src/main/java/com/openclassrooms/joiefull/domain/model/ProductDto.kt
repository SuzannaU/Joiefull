package com.openclassrooms.joiefull.domain.model

data class ProductDto(
    val id: Long,
    val name: String,
    val category: Category,
    var likes: Long,
    var rating: Int =0,
    var review: String = "",
    var isLiked: Boolean = false,
    val pictureUrl: String,
    val pictureDescription: String,
    val priceInCents: Long,
    val originalPriceInCents: Long,
)
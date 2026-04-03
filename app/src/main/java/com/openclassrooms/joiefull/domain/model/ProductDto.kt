package com.openclassrooms.joiefull.domain.model

data class ProductDto(
    val id: Int,
    val picture: String,
    val name: String,
    val category: String,
    val likes: Int = 0,
    val priceInCents: Int = 0,
    val originalPriceInCents: Int = 0,
)
//data class ProductDto(
//    val id: Int,
//    val picture: Picture,
//    val name: String,
//    val category: Category,
//    val likes: Int = 0,
//    val priceInCents: Int = 0,
//    val originalPriceInCents: Int = 0,
//)
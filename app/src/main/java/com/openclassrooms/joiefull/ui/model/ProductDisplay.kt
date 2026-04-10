package com.openclassrooms.joiefull.ui.model

import com.openclassrooms.joiefull.domain.model.Category

data class ProductDisplay(
    val id: Long,
    val name: String,
    val category: Category,
    var likes: Long,
    var rating: Int,
    var review: String,
    var isLiked: Boolean,
    val pictureUrl: String,
    val pictureDescription: String,
    val price: String,
    val originalPrice: String,
)

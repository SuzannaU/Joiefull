package com.openclassrooms.joiefull.data.entity

import com.openclassrooms.joiefull.domain.model.ProductDto

fun Clothe.toDomain(): ProductDto {
    return ProductDto(
        id = this.id,
        name = this.name,
        category = this.category,
        likes = this.likes,
        pictureUrl = this.picture.url,
        pictureDescription = this.picture.description,
        priceInCents = this.price.times(100L).toLong(),
        originalPriceInCents = this.originalPrice.times(100L).toLong(),
    )
}
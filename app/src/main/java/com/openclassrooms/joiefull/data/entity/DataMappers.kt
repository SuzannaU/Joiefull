package com.openclassrooms.joiefull.data.entity

import com.openclassrooms.joiefull.domain.model.ProductDto

fun Clothe.toDomain(): ProductDto {
    return ProductDto(
        id = this.id,
        picture = this.picture,
        name = this.name,
        category = this.category,
        likes = this.likes,
        priceInCents = this.priceInCents,
        originalPriceInCents = this.originalPriceInCents,
    )
}
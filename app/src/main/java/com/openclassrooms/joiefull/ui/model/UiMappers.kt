package com.openclassrooms.joiefull.ui.model

import com.openclassrooms.joiefull.domain.model.ProductDto
import java.util.Locale


fun ProductDto.toDisplay() : ProductDisplay {
    return ProductDisplay(
        id = this.id,
        name = this.name,
        category = this.category,
        likes = this.likes,
        pictureUrl = this.pictureUrl,
        pictureDescription = this.pictureDescription,
        price = formatPriceToString(this.priceInCents.times(100L)),
        originalPrice = formatPriceToString(this.originalPriceInCents.times(100L))
    )
}

fun formatPriceToString(price: Long): String {
    val locale = Locale.getDefault()
    return String.format(locale, "%d", price)
}
package com.openclassrooms.joiefull.ui.model

import com.openclassrooms.joiefull.domain.model.ProductDto
import java.text.NumberFormat
import java.util.Locale


fun ProductDto.toDisplay() : ProductDisplay {
    return ProductDisplay(
        id = this.id,
        name = this.name,
        category = this.category,
        likes = this.likes,
        pictureUrl = this.pictureUrl,
        pictureDescription = this.pictureDescription,
        price = formatPriceToString(this.priceInCents.div(100L)),
        originalPrice = formatPriceToString(this.originalPriceInCents.div(100L))
    )
}

fun formatPriceToString(price: Long): String {
    val locale = Locale.getDefault()
    val format = NumberFormat.getCurrencyInstance(locale)
    return format.format(price)
    //return String.format(locale, "%.2f€", price)
}
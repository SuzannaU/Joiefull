package com.openclassrooms.joiefull.domain.model

import com.openclassrooms.joiefull.R

enum class Category {
    TOPS,
    ACCESSORIES,
    BOTTOMS,
    SHOES;

    val labelId: Int
        get() = when(this) {
            TOPS -> R.string.category_tops
            ACCESSORIES -> R.string.category_accessories
            BOTTOMS -> R.string.category_bottoms
            SHOES -> R.string.category_shoes
        }

}
package com.openclassrooms.joiefull.domain.model

import com.openclassrooms.joiefull.R

enum class Category {
    TOPS,
    ACCESSORIES,
    BOTTOMS,
    SHOES;

    val labelId: Int
        get() = when(this) {
            Category.TOPS -> R.string.category_tops
            Category.ACCESSORIES -> R.string.category_accessories
            Category.BOTTOMS -> R.string.category_bottoms
            Category.SHOES -> R.string.category_shoes
        }

}
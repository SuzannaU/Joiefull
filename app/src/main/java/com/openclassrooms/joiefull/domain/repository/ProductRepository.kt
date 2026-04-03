package com.openclassrooms.joiefull.domain.repository

import com.openclassrooms.joiefull.domain.model.ProductDto

interface ProductRepository {

    suspend fun fetchProducts(): List<ProductDto>
}
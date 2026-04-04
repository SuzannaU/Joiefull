package com.openclassrooms.joiefull.domain.repository

import com.openclassrooms.joiefull.domain.model.ProductDto

interface ProductRepository {

    suspend fun fetchProductById(id: Long): ProductDto?
    suspend fun fetchProducts(): List<ProductDto>
}
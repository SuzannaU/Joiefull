package com.openclassrooms.joiefull.domain.repository

import com.openclassrooms.joiefull.domain.model.ProductDto

interface ProductRepository {

    suspend fun fetchProductById(id: Long): ProductDto?
    suspend fun fetchProducts(): List<ProductDto>
    suspend fun updateRating(id: Long, rating: Int): ProductDto?
    suspend fun updateReview(id: Long, review: String): ProductDto?
    suspend fun updateIsLiked(id: Long, isLiked: Boolean): ProductDto?
}
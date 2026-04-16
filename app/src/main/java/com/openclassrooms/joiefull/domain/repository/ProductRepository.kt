package com.openclassrooms.joiefull.domain.repository

import com.openclassrooms.joiefull.domain.model.ProductDto
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun fetchProductById(id: Long): ProductDto?
    suspend fun fetchProducts(): Flow<List<ProductDto>>
    suspend fun updateRating(id: Long, rating: Int): ProductDto?
    suspend fun updateReview(id: Long, review: String): ProductDto?
    suspend fun updateIsLiked(id: Long, isLiked: Boolean): ProductDto?
}
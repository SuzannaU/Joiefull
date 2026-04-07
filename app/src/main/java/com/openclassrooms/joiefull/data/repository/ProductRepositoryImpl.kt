package com.openclassrooms.joiefull.data.repository

import android.util.Log
import com.openclassrooms.joiefull.data.dao.ClothesApiService
import com.openclassrooms.joiefull.data.entity.toDomain
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import kotlinx.coroutines.delay

class ProductRepositoryImpl(
    private val apiService: ClothesApiService,
) : ProductRepository {

    // Products are cached for demo purposes. For live version, remote data will have to be updated
    var products = emptyList<ProductDto>()

    override suspend fun fetchProductById(id: Long): ProductDto? {
        if (products.isEmpty()) {
            return apiService.getClothes()
                .find { it.id == id }
                ?.toDomain()
        }
        return products.find { it.id == id }
    }

    override suspend fun fetchProducts(): List<ProductDto> {
        if (products.isEmpty()) {
            delay(1500L)
            products = apiService.getClothes().map { clothe -> clothe.toDomain() }
        }
        return products
    }

    override suspend fun updateRating(
        id: Long,
        rating: Int
    ): ProductDto? {
        if (products.isEmpty()) return null

        delay(1000L)
        products.filter { productDto -> productDto.id == id }
            .forEach { productDto -> productDto.rating = rating }

        return products.first { it.id == id }
    }

    override suspend fun updateReview(
        id: Long,
        review: String
    ): ProductDto? {
        Log.d("TAG", "updateReview is called")
        if (products.isEmpty()) return null

        products.filter { productDto -> productDto.id == id }
            .forEach { productDto -> productDto.review = review }

        return products.first { it.id == id }
    }

    override suspend fun updateIsLiked(
        id: Long,
        isLiked: Boolean
    ): ProductDto? {
        if (products.isEmpty()) return null

        products.filter { productDto -> productDto.id == id }
            .forEach { productDto -> productDto.isLiked = isLiked }

        return products.first { it.id == id }
    }

}
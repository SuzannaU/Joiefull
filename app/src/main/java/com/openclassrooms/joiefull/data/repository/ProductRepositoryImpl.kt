package com.openclassrooms.joiefull.data.repository

import com.openclassrooms.joiefull.data.dao.ClothesApiService
import com.openclassrooms.joiefull.data.entity.toDomain
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository

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
            products = apiService.getClothes().map { clothe -> clothe.toDomain() }
        }
        return products
    }

    override suspend fun updateRating(
        id: Long,
        rating: Int
    ): ProductDto? {
        if (products.isEmpty()) return null

        products.filter { productDto -> productDto.id == id }
            .forEach { productDto -> productDto.rating = rating }

        return products.first { it.id == id }
    }

    override suspend fun updateReview(
        id: Long,
        review: String
    ): ProductDto? {
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
            .forEach { productDto ->
                productDto.isLiked = isLiked
                productDto.likes = if (isLiked) productDto.likes+1 else productDto.likes-1
            }

        return products.first { it.id == id }
    }

}
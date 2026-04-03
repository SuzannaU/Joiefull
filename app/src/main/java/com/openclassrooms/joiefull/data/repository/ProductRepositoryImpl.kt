package com.openclassrooms.joiefull.data.repository

import com.openclassrooms.joiefull.data.dao.ClothesApiService
import com.openclassrooms.joiefull.data.entity.toDomain
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val apiService: ClothesApiService,
) : ProductRepository {

    override suspend fun fetchProducts(): List<ProductDto> {
        return apiService.getClothes().map { entity -> entity.toDomain() }
    }

}
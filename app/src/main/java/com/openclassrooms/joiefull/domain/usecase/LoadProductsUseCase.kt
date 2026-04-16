package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class LoadProductsUseCase(val repository: ProductRepository) {

    suspend fun execute(): Flow<List<ProductDto>> {
        return repository.fetchProducts()
    }
}
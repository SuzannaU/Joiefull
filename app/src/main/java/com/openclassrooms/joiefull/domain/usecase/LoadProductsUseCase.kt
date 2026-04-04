package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository

class LoadProductsUseCase(val repository: ProductRepository) {

    suspend fun execute(): List<ProductDto> {
        return repository.fetchProducts()
    }
}
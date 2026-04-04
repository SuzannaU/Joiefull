package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository

class LoadProductByIdUseCase(val repository: ProductRepository) {

    suspend fun execute(id: Long): ProductDto {
        return repository.fetchProductById(id) ?: throw NoSuchElementException()
    }
}
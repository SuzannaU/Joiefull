package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository

class UpdateRatingUseCase(val repository: ProductRepository) {
    suspend fun execute(id: Long, rating: Int): ProductDto {
        return repository.updateRating(id, rating) ?: throw NoSuchElementException()
    }
}
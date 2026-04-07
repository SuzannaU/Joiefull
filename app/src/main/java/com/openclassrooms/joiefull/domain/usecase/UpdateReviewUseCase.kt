package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository

class UpdateReviewUseCase(val repository: ProductRepository) {
    suspend fun execute(id: Long, review: String): ProductDto {
        return repository.updateReview(id, review) ?: throw NoSuchElementException()
    }
}
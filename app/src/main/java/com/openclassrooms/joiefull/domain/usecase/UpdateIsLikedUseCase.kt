package com.openclassrooms.joiefull.domain.usecase

import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository

class UpdateIsLikedUseCase(val repository: ProductRepository) {
    suspend fun execute(id: Long, isLiked: Boolean): ProductDto {
        return repository.updateIsLiked(id, isLiked) ?: throw NoSuchElementException()
    }
}
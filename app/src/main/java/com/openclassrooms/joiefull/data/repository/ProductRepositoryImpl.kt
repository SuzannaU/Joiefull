package com.openclassrooms.joiefull.data.repository

import com.openclassrooms.joiefull.data.dao.ClothesApiService
import com.openclassrooms.joiefull.data.entity.toDomain
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
The demo API only provides products data, but is not updatable. Stateflow is used to simulate a remote Data update.
 */
class ProductRepositoryImpl(
    private val apiService: ClothesApiService,
) : ProductRepository {

    // Products are cached in a stateflow for demo purposes. For live version, remote data will have to be updated
    private val _productsFlow = MutableStateFlow<List<ProductDto>>(emptyList())

    override suspend fun fetchProductById(id: Long): ProductDto? {
        if (_productsFlow.value.isEmpty()) {
            return apiService.getClothes()
                .find { it.id == id }
                ?.toDomain()
        }
        return _productsFlow.value.find { it.id == id }
    }

    override suspend fun fetchProducts(): Flow<List<ProductDto>> {
        if (_productsFlow.value.isEmpty()) {
            _productsFlow.value = apiService.getClothes().map { clothe -> clothe.toDomain() }
        }
        return _productsFlow.asStateFlow()
    }

    override suspend fun updateRating(
        id: Long,
        rating: Int
    ): ProductDto? {

        if (_productsFlow.value.isEmpty()) return null

        val currentList = _productsFlow.value
        val updatedList = currentList.map { productDto ->
            if (productDto.id == id) {
                productDto.copy(
                    rating = rating,
                )
            } else {
                productDto
            }
        }

        _productsFlow.value = updatedList
        return updatedList.first { it.id == id }
    }

    override suspend fun updateReview(
        id: Long,
        review: String
    ): ProductDto? {

        if (_productsFlow.value.isEmpty()) return null

        val currentList = _productsFlow.value
        val updatedList = currentList.map { productDto ->
            if (productDto.id == id) {
                productDto.copy(
                    review = review,
                )
            } else {
                productDto
            }
        }

        _productsFlow.value = updatedList
        return updatedList.first { it.id == id }
    }

    override suspend fun updateIsLiked(
        id: Long,
        isLiked: Boolean
    ): ProductDto? {

        if (_productsFlow.value.isEmpty()) return null

        val currentList = _productsFlow.value
        val updatedList = currentList.map { productDto ->
            if (productDto.id == id) {
                productDto.copy(
                    isLiked = isLiked,
                    likes = if (isLiked) productDto.likes + 1 else productDto.likes - 1,
                )
            } else {
                productDto
            }
        }

        _productsFlow.value = updatedList
        return updatedList.first { it.id == id }
    }
}
package com.openclassrooms.joiefull.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.domain.usecase.LoadProductByIdUseCase
import com.openclassrooms.joiefull.ui.DispatcherProvider
import com.openclassrooms.joiefull.ui.model.ProductDisplay
import com.openclassrooms.joiefull.ui.model.toDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(
    private val dispatcherProvider: DispatcherProvider,
    private val loadProductByIdUseCase: LoadProductByIdUseCase,
): ViewModel() {

    private val _productUiState = MutableStateFlow<ProductUiState>(ProductUiState.LoadingState)
    val productUiState = _productUiState.asStateFlow()

    fun loadProductById(id: Long) {
        viewModelScope.launch {
            _productUiState.value = ProductUiState.LoadingState
            withContext(dispatcherProvider.io) {
                try {
                    _productUiState.value = ProductUiState.ProductFound(
                        loadProductByIdUseCase.execute(id).toDisplay(),
                        0,
                        "",
                    )
                } catch (e: NoSuchElementException) {
                    _productUiState.value = ProductUiState.NoProduct
                }
            }
        }
    }

    // Only the in-memory product will be updated, not the remote one from the API
    fun toggleLikeState() {
        val currentState = _productUiState.value
        if (currentState !is ProductUiState.ProductFound) return

        val newLikeState = !currentState.product.isLiked
        val updatedProduct = currentState.product.copy(isLiked = newLikeState)

        _productUiState.value = currentState.copy(product = updatedProduct)
    }

    fun onCommentChanged(newComment: String) {
        val currentState = _productUiState.value
        if (currentState !is ProductUiState.ProductFound) return

        _productUiState.value = currentState.copy(review = newComment)
    }

    fun onRatingChanged(newRating: Int) {
        val currentState = _productUiState.value
        if (currentState !is ProductUiState.ProductFound) return

        _productUiState.value = currentState.copy(rating = newRating)
    }

    sealed class ProductUiState {
        object LoadingState : ProductUiState()
        object NoProduct : ProductUiState()

        data class ProductFound(
            val product: ProductDisplay,
            val rating: Int,
            val review: String,
        ) : ProductUiState()
    }
}
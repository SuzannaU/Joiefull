package com.openclassrooms.joiefull.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.domain.usecase.LoadProductByIdUseCase
import com.openclassrooms.joiefull.domain.usecase.UpdateIsLikedUseCase
import com.openclassrooms.joiefull.domain.usecase.UpdateRatingUseCase
import com.openclassrooms.joiefull.domain.usecase.UpdateReviewUseCase
import com.openclassrooms.joiefull.ui.DispatcherProvider
import com.openclassrooms.joiefull.ui.model.ProductDisplay
import com.openclassrooms.joiefull.ui.model.toDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(
    private val savedState: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider,
    private val loadProductByIdUseCase: LoadProductByIdUseCase,
    private val updateIsLikedUseCase: UpdateIsLikedUseCase,
    private val updateRatingUseCase: UpdateRatingUseCase,
    private val updateReviewUseCase: UpdateReviewUseCase,
) : ViewModel() {

    private val _productUiState = MutableStateFlow<ProductUiState>(ProductUiState.LoadingState)
    val productUiState = _productUiState.asStateFlow()

    fun loadProductById(id: Long) {

        viewModelScope.launch {
            _productUiState.value = ProductUiState.LoadingState
            withContext(dispatcherProvider.io) {
                try {
                    val product = withContext(dispatcherProvider.io) {
                        loadProductByIdUseCase.execute(id)
                    }
                    val reviewState =
                        savedState.get<String>("reviewState_product$id") ?: product.review
                    _productUiState.value = ProductUiState.ProductFound(
                        product.toDisplay().copy(review = reviewState)
                    )
                } catch (e: NoSuchElementException) {
                    _productUiState.value = ProductUiState.NoProduct
                }
            }
        }
    }

    fun toggleLikeState() {
        val currentState = _productUiState.value
        if (currentState !is ProductUiState.ProductFound) return

        val newLikeState = !currentState.product.isLiked

        viewModelScope.launch {
            _productUiState.value = ProductUiState.LoadingState

            try {
                val updatedProduct = withContext(dispatcherProvider.io) {
                    updateIsLikedUseCase.execute(currentState.product.id, newLikeState)
                }
                _productUiState.value = currentState.copy(product = updatedProduct.toDisplay())
            } catch (e: NoSuchElementException) {
                _productUiState.value = ProductUiState.NoProduct
            }
        }
    }

    fun onReviewChanged(newReview: String) {
        val currentState = _productUiState.value
        if (currentState !is ProductUiState.ProductFound) return

        savedState["reviewState_product${currentState.product.id}"] = newReview

        _productUiState.value = currentState.copy(
            product = currentState.product.copy(
                review = newReview
            )
        )
    }

    fun onReviewSubmitted(reviewText: String) {
        val currentState = _productUiState.value
        if (currentState !is ProductUiState.ProductFound) return

        viewModelScope.launch {
            _productUiState.value = ProductUiState.LoadingState
            try {
                val updatedProduct = withContext(dispatcherProvider.io) {
                    updateReviewUseCase.execute(currentState.product.id, reviewText)
                }
                _productUiState.value = currentState.copy(product = updatedProduct.toDisplay())

            } catch (e: NoSuchElementException) {
                _productUiState.value = ProductUiState.NoProduct
            }
        }
    }


    fun onRatingChanged(newRating: Int) {
        val currentState = _productUiState.value
        if (currentState !is ProductUiState.ProductFound) return

        viewModelScope.launch {
            _productUiState.value = ProductUiState.LoadingState
            try {
                val updatedProduct = withContext(dispatcherProvider.io) {
                    updateRatingUseCase.execute(currentState.product.id, newRating)
                }
                _productUiState.value = currentState.copy(product = updatedProduct.toDisplay())

            } catch (e: NoSuchElementException) {
                _productUiState.value = ProductUiState.NoProduct
            }
        }
    }

    sealed class ProductUiState {
        object LoadingState : ProductUiState()
        object NoProduct : ProductUiState()

        data class ProductFound(
            val product: ProductDisplay,
        ) : ProductUiState()
    }
}
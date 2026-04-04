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
                        loadProductByIdUseCase.execute(id).toDisplay()
                    )
                } catch (e: NoSuchElementException) {
                    _productUiState.value = ProductUiState.NoProduct
                }
            }
        }
    }


    sealed class ProductUiState {
        object LoadingState : ProductUiState()
        object NoProduct : ProductUiState()

        data class ProductFound(
            val product: ProductDisplay
        ) : ProductUiState()
    }
}
package com.openclassrooms.joiefull.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.domain.usecase.LoadProductsUseCase
import com.openclassrooms.joiefull.ui.DispatcherProvider
import com.openclassrooms.joiefull.ui.model.ProductDisplay
import com.openclassrooms.joiefull.ui.model.toDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogViewModel(
    private val dispatcherProvider: DispatcherProvider,
    private val loadProductsUseCase: LoadProductsUseCase,
) : ViewModel() {

    private val _catalogUiState = MutableStateFlow<CatalogUiState>(CatalogUiState.LoadingState)
    val catalogUiState = _catalogUiState.asStateFlow()

    fun loadAllProducts() {
        viewModelScope.launch {
            _catalogUiState.value = CatalogUiState.LoadingState
            withContext(dispatcherProvider.io) {
                _catalogUiState.value = CatalogUiState.ProductsFound(
                        loadProductsUseCase.execute().map { it.toDisplay() }
                    )
            }
        }
    }

    sealed class CatalogUiState {
        object LoadingState : CatalogUiState()

        data class ProductsFound(
            val products: List<ProductDisplay>,
        ) : CatalogUiState()

    }
}
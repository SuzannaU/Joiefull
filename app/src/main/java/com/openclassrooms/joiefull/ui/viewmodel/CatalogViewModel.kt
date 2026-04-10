package com.openclassrooms.joiefull.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.domain.model.Category
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

    init {
        loadAllProducts()
    }

    fun loadAllProducts() {

        viewModelScope.launch {
            _catalogUiState.value = CatalogUiState.LoadingState
            withContext(dispatcherProvider.io) {
                val products = loadProductsUseCase.execute().map { it.toDisplay() }
                _catalogUiState.value = CatalogUiState.ProductsFound(
                    allProducts = products,
                    groupedProducts = products.groupBy { it.category },
                )
            }
        }
    }

    sealed class CatalogUiState {
        object LoadingState : CatalogUiState()

        data class ProductsFound(
            val allProducts: List<ProductDisplay>,
            val groupedProducts: Map<Category, List<ProductDisplay>>,
        ) : CatalogUiState()

    }
}
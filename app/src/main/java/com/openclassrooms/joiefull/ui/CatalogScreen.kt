package com.openclassrooms.joiefull.ui

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.ui.viewmodel.CatalogViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = koinViewModel(),
) {

    val uiState = viewModel.catalogUiState.collectAsStateWithLifecycle()
    val categoryListState = rememberLazyListState()
//    val isJumpToTopVisible = rememberSaveable {
//        derivedStateOf { categoryListState.firstVisibleItemIndex > 0 }
//    }
    val categories = Category.entries
    viewModel.loadAllProducts()

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.padding(innerPadding),
        ) {
            when (uiState.value) {
                CatalogViewModel.CatalogUiState.LoadingState -> {
                    Text(text = "loading...")
                }

                is CatalogViewModel.CatalogUiState.ProductsFound -> {

                    LazyColumn(
                        state = categoryListState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = categoryListState),
                    ) {
                        val products = (uiState.value as CatalogViewModel.CatalogUiState.ProductsFound).products
                        categories.forEach { category ->
                            val catProducts = products.filter { it.category == category }
                            item {
                                Text(
                                    text = category.name
                                )
                                LazyRow(
                                    modifier = Modifier.height(282.dp)
                                ) {
                                    items(catProducts) { product ->
                                        Column {
                                            PictureBox(
                                                pictureUrl = product.pictureUrl,
                                                pictureDescription = product.pictureDescription,
                                                likes = product.likes.toString(),
                                            )
                                            ProductDetails(
                                                productName = product.name,
                                                globalRating = "4.3",
                                                price = product.price,
                                                originalPrice = product.originalPrice,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
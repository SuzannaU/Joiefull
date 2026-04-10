package com.openclassrooms.joiefull.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.ui.components.LoadingBar
import com.openclassrooms.joiefull.ui.components.PictureBoxCatalog
import com.openclassrooms.joiefull.ui.components.ProductDetails
import com.openclassrooms.joiefull.ui.model.ProductDisplay
import com.openclassrooms.joiefull.ui.viewmodel.CatalogViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = koinViewModel(),
    onProductClicked: (Long) -> Unit,
) {

    val uiState = viewModel.catalogUiState.collectAsStateWithLifecycle()
    viewModel.loadAllProducts()

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->

        when (uiState.value) {
            CatalogViewModel.CatalogUiState.LoadingState -> {
                LoadingBar(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                )
            }

            is CatalogViewModel.CatalogUiState.ProductsFound -> {
                CatalogContent(
                    products =
                        (uiState.value as CatalogViewModel.CatalogUiState.ProductsFound).products,
                    onProductClicked = onProductClicked,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun CatalogContent(
    products: List<ProductDisplay>,
    onProductClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val categories = Category.entries

    val rowStates = remember(categories) {
        categories.associate { it.name to LazyListState() }
    }

    val isJumpToTopVisible = remember {
        derivedStateOf { categoryListState.firstVisibleItemIndex > 0 }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = categoryListState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = categoryListState),
        ) {
            categories.forEach { category ->

                val catProducts =
                    products.filter { it.category == category }   // move filtering of products in ViewModel

                item(key = category.name) {
                    CategoryRow(
                        category = category,
                        products = catProducts,
                        listState = rowStates[category.name] ?: rememberLazyListState(),
                        onProductClicked = onProductClicked,
                    )
                }
            }
        }

        if (isJumpToTopVisible.value) {
            JumpToButton(
                painter = painterResource(R.drawable.arrow_upward),
                contentDescription = stringResource(R.string.navigate_to_top),
                onclick = {
                    scope.launch {
                        categoryListState.animateScrollToItem(0)
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryRow(
    category: Category,
    products: List<ProductDisplay>,
    listState: LazyListState,
    onProductClicked: (Long) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val isJumpToFirstVisible = remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    Column {
        Text(
            text = stringResource(category.labelId),
            style = MaterialTheme.typography.titleLarge
        )

        Box(
            contentAlignment = Alignment.CenterEnd,
        ) {
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                modifier = Modifier.height(300.dp)
            ) {
                items(products) { product ->
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .width(200.dp)
                            .clickable {
                                onProductClicked(product.id)
                            }

                    ) {
                        PictureBoxCatalog(
                            pictureUrl = product.pictureUrl,
                            pictureDescription = product.pictureDescription,
                            likes = product.likes.toString(),
                            isLiked = product.isLiked,
                        )
                        ProductDetails(
                            productName = product.name,
                            globalRating = "4.3",
                            price = product.price,
                            originalPrice = product.originalPrice,
                            modifier = Modifier.fillMaxWidth(),
                            forCatalogScreen = true,
                        )
                    }
                }
            }

            if (isJumpToFirstVisible.value) {
                JumpToButton(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.navigate_to_start),
                    onclick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun JumpToButton(
    painter: Painter,
    contentDescription: String,
    onclick: () -> Unit
) {
    IconButton(
        onClick = onclick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(4.dp)
        )
    }
}
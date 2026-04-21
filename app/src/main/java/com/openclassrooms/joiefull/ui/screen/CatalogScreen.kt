package com.openclassrooms.joiefull.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.ui.components.LoadingBar
import com.openclassrooms.joiefull.ui.components.NoProductFound
import com.openclassrooms.joiefull.ui.components.PictureBoxCatalog
import com.openclassrooms.joiefull.ui.components.ProductDetails
import com.openclassrooms.joiefull.ui.model.ProductDisplay
import com.openclassrooms.joiefull.ui.viewmodel.CatalogViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = koinViewModel(),
    onProductClicked: (Long) -> Unit,
) {

    val uiState = viewModel.catalogUiState.collectAsStateWithLifecycle()

    when (uiState.value) {
        CatalogViewModel.CatalogUiState.LoadingState -> {
            LoadingBar(
                modifier = Modifier.fillMaxSize(),
            )
        }

        is CatalogViewModel.CatalogUiState.ProductsFound -> {
            CatalogContent(
                groupedProducts =
                    (uiState.value as CatalogViewModel.CatalogUiState.ProductsFound).groupedProducts,
                onProductClicked = onProductClicked,
            )
        }

        CatalogViewModel.CatalogUiState.NoProducts -> {
            NoProductFound(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun CatalogContent(
    groupedProducts: Map<Category, List<ProductDisplay>>,
    onProductClicked: (Long) -> Unit,
) {
    val categoryListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val categories = Category.entries
    val isJumpToTopVisible = remember {
        derivedStateOf { categoryListState.firstVisibleItemIndex > 0 }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize(),
    ) {
        CategoriesColumn(
            categoryListState = categoryListState,
            categories = categories,
            groupedProducts = groupedProducts,
            onProductClicked = onProductClicked,
        )

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
fun CategoriesColumn(
    categoryListState: LazyListState,
    categories: List<Category>,
    groupedProducts: Map<Category, List<ProductDisplay>>,
    onProductClicked: (Long) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val focusRequesters = remember { mutableMapOf<String, FocusRequester>() }

    LazyColumn(
        state = categoryListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.forEachIndexed { index, category ->

            val catProducts = groupedProducts[category] ?: emptyList()

            item(key = category.name) {
                val rowState = rememberSaveable(
                    saver = LazyListState.Saver
                ) {
                    LazyListState()
                }
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(focusRequester) {
                    focusRequesters[category.name] = focusRequester
                }

                CategoryRow(
                    category = category,
                    products = catProducts,
                    listState = rowState,
                    index = index,
                    lastIndex = categories.lastIndex,
                    focusRequester = focusRequester,
                    nextCat = {
                        scope.launch {
                            val nextIndex = index + 1
                            categoryListState.scrollToItem(nextIndex)
                            delay(100)
                            focusRequesters[categories[nextIndex].name]?.requestFocus()
                        }
                    },
                    prevCat = {
                        scope.launch {
                            val prevIndex = index - 1
                            categoryListState.scrollToItem(prevIndex)
                            delay(100)
                            focusRequesters[categories[prevIndex].name]?.requestFocus()
                        }
                    },
                    onProductClicked = onProductClicked,
                )
            }
        }
    }
}

@Composable
fun CategoryRow(
    category: Category,
    products: List<ProductDisplay>,
    listState: LazyListState,
    index: Int,
    lastIndex: Int,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    nextCat: () -> Unit,
    prevCat: () -> Unit,
    onProductClicked: (Long) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val isJumpToFirstVisible = remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    Column(
        modifier = modifier,
    ) {
        CategoryTitle(
            category = category,
            focusRequester = focusRequester,
            index = index,
            lastIndex = lastIndex,
            nextCat = nextCat,
            prevCat = prevCat,
        )

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                modifier = Modifier
                    .height(280.dp)
                    .fillMaxWidth()
            ) {
                items(products) { product ->
                    ProductColumn(
                        product = product,
                        onProductClicked = onProductClicked,
                    )
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
fun ProductColumn(
    product: ProductDisplay,
    onProductClicked: (Long) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .width(200.dp)
            .clickable {
                onProductClicked(product.id)
            }
            .semantics(mergeDescendants = true) {}
    ) {
        PictureBoxCatalog(
            pictureUrl = product.pictureUrl,
            pictureDescription = product.pictureDescription,
            likes = product.likes.toString(),
            isLiked = product.isLiked,
        )
        ProductDetails(
            productName = product.name,
            globalRating = "4.3",   // For demo purposes. Rating is not provided in demo API
            price = product.price,
            originalPrice = product.originalPrice,
            modifier = Modifier.fillMaxWidth(),
            forCatalogScreen = true,
        )
    }
}

@Composable
fun CategoryTitle(
    category: Category,
    focusRequester: FocusRequester,
    index: Int,
    lastIndex: Int,
    nextCat: () -> Unit,
    prevCat: () -> Unit,
) {
    val nextCat = stringResource(R.string.next_category)
    val prevCat = stringResource(R.string.prev_category)
    Text(
        text = stringResource(category.labelId),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable()
            .semantics {
                heading()
                val actions = mutableListOf<CustomAccessibilityAction>()

                if (index < lastIndex) {
                    actions += CustomAccessibilityAction(
                        label = nextCat,
                        action = {
                            nextCat()
                            true
                        }
                    )
                }

                if (index > 0) {
                    actions += CustomAccessibilityAction(
                        label = prevCat,
                        action = {
                            prevCat()
                            true
                        }
                    )
                }
                customActions = actions
            }
    )
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
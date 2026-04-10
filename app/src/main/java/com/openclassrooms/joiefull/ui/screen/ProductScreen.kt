package com.openclassrooms.joiefull.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.ui.components.LoadingBar
import com.openclassrooms.joiefull.ui.components.PictureBoxProduct
import com.openclassrooms.joiefull.ui.components.ProductDetails
import com.openclassrooms.joiefull.ui.components.Review
import com.openclassrooms.joiefull.ui.components.ShareDialog
import com.openclassrooms.joiefull.ui.components.UserRating
import com.openclassrooms.joiefull.ui.model.ProductDisplay
import com.openclassrooms.joiefull.ui.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductScreen(
    productId: Long,
    userPicture: String,
    onBackClicked: () -> Unit,
    shareProduct: (String, String) -> Unit,
    showReviewToast: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = koinViewModel(),
) {

    val scrollState = rememberScrollState()
    val uiState = viewModel.productUiState.collectAsStateWithLifecycle()
    viewModel.loadProductById(productId)

    val showShareDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->

        when (uiState.value) {
            ProductViewModel.ProductUiState.LoadingState -> {
                LoadingBar(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                )
            }

            ProductViewModel.ProductUiState.NoProduct -> {
                Text(text = "No product")
            }

            is ProductViewModel.ProductUiState.ProductFound -> {
                val product =
                    (uiState.value as ProductViewModel.ProductUiState.ProductFound).product
                ProductContent(
                    product = product,
                    userPicture = userPicture,
                    userRating = product.rating,
                    reviewText = product.review,
                    onBackClicked = onBackClicked,
                    onShareClicked = { showShareDialog.value = true },
                    onLikeClicked = { viewModel.toggleLikeState() },
                    onRatingChanged = { viewModel.onRatingChanged(it) },
                    onReviewChanged = { reviewText, reviewConfirmation ->
                        viewModel.onReviewChanged(reviewText)
                        showReviewToast(reviewConfirmation)
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .verticalScroll(state = scrollState),
                )

                if (showShareDialog.value) {
                    ShareDialog(
                        onDismissRequest = { showShareDialog.value = false },
                        onConfirmation = { comment ->
                            showShareDialog.value = false
                            val textToShare = buildString {
                                if (comment.isNotBlank()) {
                                    append(comment)
                                    append("\n\n")
                                }
                                append(product.pictureUrl)
                            }
                            shareProduct(product.name, textToShare)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ProductContent(
    product: ProductDisplay,
    userPicture: String,
    userRating: Int,
    reviewText: String,
    onRatingChanged: (Int) -> Unit,
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onLikeClicked: () -> Unit,
    onReviewChanged: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {

        PictureBoxProduct(
            pictureUrl = product.pictureUrl,
            pictureDescription = product.pictureDescription,
            likes = product.likes.toString(),
            onBackClicked = onBackClicked,
            onShareClicked = onShareClicked,
            onLikeClicked = onLikeClicked,
            isLiked = product.isLiked,
        )
        ProductDetails(
            productName = product.name,
            globalRating = "3,2",
            price = product.price,
            originalPrice = product.originalPrice,
            forCatalogScreen = false,
        )
        Text(
            text = stringResource(R.string.product_description),
            style = MaterialTheme.typography.bodyMedium
        )
        UserRating(
            userPicture = userPicture,
            userRating = userRating,
            onRatingChanged = onRatingChanged
        )
        Review(
            reviewText = reviewText,
            onReviewChanged = onReviewChanged,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProductScreenPreview() {
//    JoiefullTheme {
//        ProductScreen(
//            productId = 1,
//            product = ProductDisplay(
//                id = 1,
//                name = "Pull torsadé",
//                category = Category.TOPS,
//                likes = 102,
//                pictureUrl = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/tops/2.jpg",
//                pictureDescription = "description de l'image",
//                price = "49,99 €",
//                originalPrice = "59,99 €"
//            ),
//            userPicture = "https://randomuser.me/api/portraits/men/1.jpg",
//            onBackClicked = {},
//            onShareClicked = {},
//        )
//    }
//}
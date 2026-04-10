package com.openclassrooms.joiefull.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openclassrooms.joiefull.ui.screen.CatalogScreen
import com.openclassrooms.joiefull.ui.screen.ProductScreen

@Composable
fun Navigation(
    modifier: Modifier,
    shareProduct: (String, String) -> Unit,
    showReviewToast: (String) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "catalog"
    ) {

        composable("catalog") {
            CatalogScreen(
                onProductClicked = { productId ->
                    navController.navigate("product/$productId")
                },
                modifier = modifier
                    .fillMaxSize(),
            )
        }

        composable(
            route = "product/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val productId = backStackEntry.arguments?.getLong("productId") ?: 0

            ProductScreen(
                modifier = modifier
                    .fillMaxSize(),
                productId = productId,
                userPicture = "https://randomuser.me/api/portraits/men/1.jpg",
                onBackClicked = {
                    navController.navigate("catalog")
                },
                shareProduct = shareProduct,
                showReviewToast = showReviewToast,
            )
        }
    }
}
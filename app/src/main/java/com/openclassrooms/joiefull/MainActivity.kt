package com.openclassrooms.joiefull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openclassrooms.joiefull.ui.screen.CatalogScreen
import com.openclassrooms.joiefull.ui.screen.ProductScreen
import com.openclassrooms.joiefull.ui.theme.JoiefullTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoiefullTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "catalog"
    ) {
        composable("catalog") {
            CatalogScreen(
                onProductClicked = { productId ->
                    navController.navigate("product/$productId")
                },
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable(
            route = "product/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.LongType
            })
        ) { backStackEntry ->

            val productId = backStackEntry.arguments?.getLong("productId") ?: 0

            ProductScreen(
                modifier = Modifier
                    .fillMaxSize(),
                productId = productId,
                userPicture = "https://randomuser.me/api/portraits/men/1.jpg",
                onBackClicked = {
                    navController.navigate("catalog")
                },
                onShareClicked = {},
            )
        }

    }
}
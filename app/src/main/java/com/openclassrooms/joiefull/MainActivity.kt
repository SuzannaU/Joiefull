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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.ui.model.ProductDisplay
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
                onProductClicked = {
                    navController.navigate("product")
                },
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable("product") {
            ProductScreen(
                modifier = Modifier
                    .fillMaxSize(),
                product = ProductDisplay(
                    id = 1,
                    name = "Pull torsadé",
                    category = Category.TOPS,
                    likes = 102,
                    pictureUrl = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/tops/2.jpg",
                    pictureDescription = "description de l'image",
                    price = "49,99 €",
                    originalPrice = "59,99 €"
                ),
                userPicture = "https://randomuser.me/api/portraits/men/1.jpg",
                userRating = 4,
                onBackClicked = {
                    navController.navigate("catalog")
                },
                onShareClicked = {},
            )
        }

    }
}
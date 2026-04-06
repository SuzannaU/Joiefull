package com.openclassrooms.joiefull.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.ui.components.PictureBoxProduct
import com.openclassrooms.joiefull.ui.components.ProductDetails
import com.openclassrooms.joiefull.ui.components.Review
import com.openclassrooms.joiefull.ui.components.UserRating
import com.openclassrooms.joiefull.ui.model.ProductDisplay
import com.openclassrooms.joiefull.ui.theme.JoiefullTheme

@Composable
fun ProductScreen(
    product: ProductDisplay,
    userPicture: String,
    userRating: Int,
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 12.dp),
        ) {

            PictureBoxProduct(
                pictureUrl = product.pictureUrl,
                pictureDescription = product.pictureDescription,
                likes = product.likes.toString(),
                onBackClicked = onBackClicked,
                onShareClicked = onShareClicked,
            )
            ProductDetails(
                productName = product.name,
                globalRating = "3,2",
                price = product.price,
                originalPrice = product.originalPrice,
                forCatalogScreen = false,
            )
            Text(
                text = "Pull vert forêt à motif torsadé élégant, tricot finement travaillé avec manches bouffantes et col montant; doux et chaleureux.",
                style = MaterialTheme.typography.bodyMedium
            )
            UserRating(
                userPicture = userPicture,
                userRating = userRating,
            )
            Review(
                onReviewChange = {},
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    JoiefullTheme {
        ProductScreen(
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
            onBackClicked = {},
            onShareClicked = {},
        )
    }
}
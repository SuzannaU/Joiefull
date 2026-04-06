package com.openclassrooms.joiefull.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.ui.theme.JoiefullTheme

@Composable
fun ProductDetails(
    productName: String,
    globalRating: String,
    price: String,
    originalPrice: String,
    forCatalogScreen: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (forCatalogScreen) 8.dp else 0.dp, vertical = 8.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = productName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = if (forCatalogScreen) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(0.70f)
            )
            GlobalRating(
                globalRating = globalRating,
                forCatalogScreen = forCatalogScreen,
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = price,
                style = if (forCatalogScreen) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = originalPrice,
                textDecoration = TextDecoration.LineThrough,
                color = Color.Gray,
                style = if (forCatalogScreen) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun GlobalRating(
    globalRating: String,
    forCatalogScreen: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {

        Icon(
            painterResource(R.drawable.baseline_star_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = globalRating,
            style = if (forCatalogScreen) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsCatalogPreview() {
    JoiefullTheme {
        ProductDetails(
            productName = "Pull torsadé",
            globalRating = "4.4",
            price = "49,00€",
            originalPrice = "59€",
            forCatalogScreen = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsProductPreview() {
    JoiefullTheme {
        ProductDetails(
            productName = "Pull torsadé",
            globalRating = "4.4",
            price = "49,00€",
            originalPrice = "59€",
            forCatalogScreen = false,
        )
    }
}
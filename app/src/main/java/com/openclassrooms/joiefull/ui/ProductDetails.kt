package com.openclassrooms.joiefull.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
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
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(0.70f)
            )
            GlobalRating(
                globalRating = globalRating,
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = price,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = originalPrice,
                textDecoration = TextDecoration.LineThrough,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun GlobalRating(
    globalRating: String,
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
            modifier = Modifier
        )

        Text(
            text = globalRating,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview() {
    JoiefullTheme {
        ProductDetails(
            productName = "Pull torsadé",
            globalRating = "4.4",
            price = "49,00€",
            originalPrice = "59€",
        )
    }
}
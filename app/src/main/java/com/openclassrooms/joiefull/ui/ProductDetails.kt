package com.openclassrooms.joiefull.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joiefull.R

@Composable
fun ProductDetails(
    productName: String,
    globalRating: String,
    price: String,
    originalPrice: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = productName,
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
            )

            Text(
                text = originalPrice,
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
        modifier = modifier,
    ) {

        Icon(
            painterResource(R.drawable.baseline_star_24),
            contentDescription = null,
            modifier = Modifier
        )

        Text(
            text = globalRating
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview() {
    ProductDetails(
        productName = "Pull torsadé",
        globalRating = "4.4",
        price = "49€",
        originalPrice = "59€",
    )
}
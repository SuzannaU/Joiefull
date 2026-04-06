package com.openclassrooms.joiefull.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.openclassrooms.joiefull.R

@Composable
fun UserRating(
    userPicture: String,
    userRating: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        AsyncImage(
            model = userPicture,
            contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )
        StarRating(
            rating = userRating,
            onRatingChanged = {},
        )
    }
}

@Composable
fun StarRating(
    rating: Int,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            val isFilled = rating > index

            IconButton(
                onClick = { onRatingChanged(index + 1f) }
            ) {
                Icon(
                    painter = painterResource(if (isFilled) R.drawable.star_filled else R.drawable.star_outlined),
                    contentDescription = "Rating is ${index + 1}",
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Composable
fun Review(
    onReviewChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var reviewText = remember { mutableStateOf("") }

    OutlinedTextField(
        value = reviewText.value,
        placeholder = {
            Text("Commentaire")
        },
        label = {
            Text(
                text = "Partagez ici vos impressions sur cette pièce",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        },
        onValueChange = { onReviewChange(it) },
        modifier = modifier
    )
}
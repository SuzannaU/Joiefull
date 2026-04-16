package com.openclassrooms.joiefull.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.openclassrooms.joiefull.R

@Composable
fun UserRating(
    userPicture: String,
    userRating: Int,
    onRatingChanged: (Int) -> Unit,
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
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        StarRating(
            rating = userRating,
            onRatingChanged = onRatingChanged,
        )
    }
}

@Composable
fun StarRating(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            val isFilled = rating > index

            IconButton(
                onClick = { onRatingChanged(index + 1) }
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
    reviewText: String,
    onReviewChanged: (String) -> Unit,
    onReviewSubmitted: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val reviewConfirmation = stringResource(R.string.review_saved)

    OutlinedTextField(
        value = reviewText,
        onValueChange = onReviewChanged,
        placeholder = {
            Text(
                text = stringResource(R.string.review_placeholder),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        label = {
            Text(
                text = stringResource(R.string.review_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onReviewSubmitted(reviewText, reviewConfirmation) }
        ),
        modifier = modifier
    )
}
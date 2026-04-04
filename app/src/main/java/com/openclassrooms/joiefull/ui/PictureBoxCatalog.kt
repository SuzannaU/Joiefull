package com.openclassrooms.joiefull.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.openclassrooms.joiefull.R


@Composable
fun PictureBox(
    pictureUrl: String,
    pictureDescription: String,
    likes: String,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = pictureUrl,
            contentDescription = pictureDescription,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Likes(
            likes = likes,
            modifier = Modifier
                .padding(12.dp)
                .size(height = 27.dp, width = 50.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun Likes(
    likes: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier,
    ) {

        Icon(
            painterResource(R.drawable.outline_favorite_24),
            contentDescription = null,
            modifier = Modifier.size(15.dp)
        )

        Text(
            text = likes
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PictureBoxPreview() {
    PictureBox(
        pictureUrl = "",
        pictureDescription = "description",
        likes = "53"
    )
}
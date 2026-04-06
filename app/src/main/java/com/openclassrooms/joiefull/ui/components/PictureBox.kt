package com.openclassrooms.joiefull.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.ui.theme.JoiefullTheme


@Composable
fun PictureBoxCatalog(
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
            contentScale = ContentScale.Crop,
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
fun PictureBoxProduct(
    pictureUrl: String,
    pictureDescription: String,
    likes: String,
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
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
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(431.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_upward),
                contentDescription = "Naviguer vers le catalogue",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }

        IconButton(
            onClick = onShareClicked,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.share),
                contentDescription = "Naviguer vers le catalogue",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }

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
            text = likes,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PictureBoxPreview() {
    JoiefullTheme {
        PictureBoxCatalog(
            pictureUrl = "",
            pictureDescription = "description",
            likes = "53"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PictureBoxProductPreview() {
    JoiefullTheme {
        PictureBoxProduct(
            pictureUrl = "",
            pictureDescription = "description",
            likes = "53",
            onBackClicked = {},
            onShareClicked = {}
        )
    }
}
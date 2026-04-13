package com.openclassrooms.joiefull.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.R

@Composable
fun CommentScreen(
    pictureUrl: String,
    onDismiss: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    var textState = rememberTextFieldState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.enter_comment),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(24.dp),
        )
        TextField(
            state = textState,
            label = {
                Text(
                    text = stringResource(R.string.review_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp),
            ) {
                Text(stringResource(R.string.dismiss))
            }
            Button(
                onClick = {
                    onConfirmation(buildTextToShare(textState.text, pictureUrl))
                },
                modifier = Modifier.padding(8.dp),
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    }
}

private fun buildTextToShare(comment: CharSequence, pictureUrl: String): String {
    val textToShare = buildString {
        if (comment.isNotBlank()) {
            append(comment)
            append("\n\n")
        }
        append(pictureUrl)
    }
    return textToShare
}
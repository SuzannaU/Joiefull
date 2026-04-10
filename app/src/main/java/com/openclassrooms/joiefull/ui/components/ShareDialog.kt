package com.openclassrooms.joiefull.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.ui.theme.JoiefullTheme

@Composable
fun ShareDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        var state = rememberTextFieldState()

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier,
        ) {
            Text(
                text = stringResource(R.string.enter_comment),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(24.dp),
            )
            TextField(
                state = state,
                label = {
                    Text(
                        text = stringResource(R.string.review_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                onKeyboardAction = { performDefaultAction ->
                    onConfirmation(state.text.toString())
                    performDefaultAction()
                },
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
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.dismiss))
                }
                Button(
                    onClick = { onConfirmation(state.text.toString()) },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        }
    }
}

@Preview
@Composable
fun ShareDialogPreview() {
    JoiefullTheme {
        ShareDialog(
            onDismissRequest = {},
            onConfirmation = {},
        )
    }
}
package com.openclassrooms.joiefull

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.openclassrooms.joiefull.ui.screen.MainScreen
import com.openclassrooms.joiefull.ui.theme.JoiefullTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoiefullTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        shareProduct = { name, textToShare ->
                            share(name, textToShare)
                        },
                        showReviewToast = { reviewConfirmation ->
                            showToast(reviewConfirmation, Toast.LENGTH_SHORT)
                        },
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    )
                }
            }
        }
    }

    private fun share(name: String, textToShare: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, textToShare)
            putExtra(
                Intent.EXTRA_TITLE,
                getString(R.string.share_this_item, name)
            )
        }

        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun showToast(text: String, duration: Int) {
        Toast.makeText(
            this,
            text,
            duration
        ).show()
    }
}
package com.openclassrooms.joiefull

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.openclassrooms.joiefull.ui.components.NoProductSelected
import com.openclassrooms.joiefull.ui.screen.CatalogScreen
import com.openclassrooms.joiefull.ui.screen.CommentScreen
import com.openclassrooms.joiefull.ui.screen.ProductScreen
import com.openclassrooms.joiefull.ui.theme.JoiefullTheme
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(
    shareProduct: (String, String) -> Unit,
    showReviewToast: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val paneExpansionState = rememberPaneExpansionState()
    paneExpansionState.setFirstPaneProportion(0.66f)
    val scope = rememberCoroutineScope()

    val productIdState = rememberSaveable { mutableLongStateOf(-1L) }
    val productNameState = rememberSaveable { mutableStateOf("") }
    val pictureUrlState = rememberSaveable { mutableStateOf("") }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        paneExpansionState = paneExpansionState,
        modifier = modifier,
        listPane = {
            AnimatedPane {
                CatalogScreen(
                    onProductClicked = { productId ->
                        productIdState.longValue = productId
                        scope.launch {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                            )
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {

                val selectedId = productIdState.longValue
                Log.d("TAG", "selected is $selectedId")

                if (selectedId == -1L) {
                    NoProductSelected(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ProductScreen(
                        productId = selectedId,
                        userPicture = "https://randomuser.me/api/portraits/men/1.jpg",
                        onBackClicked = {
                            scope.launch {
                                navigator.navigateBack(
                                    backNavigationBehavior = BackNavigationBehavior.PopUntilScaffoldValueChange
                                )
                            }
                        },
                        onShareClicked = { productName, pictureUrl ->
                            productNameState.value = productName
                            pictureUrlState.value = pictureUrl
                            scope.launch {
                                navigator.navigateTo(
                                    pane = ListDetailPaneScaffoldRole.Extra,
                                )
                            }
                        },
                        showReviewToast = showReviewToast,
                    )
                }
            }
        },
        extraPane = {
            AnimatedPane {

                CommentScreen(
                    pictureUrl = pictureUrlState.value,
                    onDismiss = {
                        scope.launch {
                            navigator.navigateBack(
                                backNavigationBehavior = BackNavigationBehavior.PopUntilScaffoldValueChange
                            )
                        }
                    },
                    onConfirmation = { textToShare ->
                        shareProduct(productNameState.value, textToShare)
                    },
                )
            }
        }
    )
}
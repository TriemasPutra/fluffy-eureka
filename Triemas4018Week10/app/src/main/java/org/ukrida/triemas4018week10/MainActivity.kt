package org.ukrida.triemas4018week10

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.ukrida.triemas4018week10.data.remote.RemoteDataSource
import org.ukrida.triemas4018week10.ui.screens.ProductScreen
import org.ukrida.triemas4018week10.ui.theme.Triemas4018Week10Theme
import org.ukrida.triemas4018week10.ui.viewmodels.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            try {
                val json = RemoteDataSource()
                    .fetchRawProducts()

                Log.d("Electronic_Products", json)
            } catch (e: Exception) {
                Log.e("Electronic_Products", "Failed to fetch product data", e)
            }
        }
        enableEdgeToEdge()
        setContent {
            Triemas4018Week10Theme {
                val viewModel: ProductViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                Surface(modifier = Modifier) {
                    ProductScreen(
                        uiState = uiState,
                        onSearchChange = viewModel::searchProducts,
                        onProductClick = viewModel::selectProduct,
                        onBackToList = {
                            viewModel.selectProduct(-1)
                        },
                        onRetry = viewModel::loadProducts
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Triemas4018Week10Theme {
        Greeting("Android")
    }
}

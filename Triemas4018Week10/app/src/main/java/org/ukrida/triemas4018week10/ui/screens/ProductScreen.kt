package org.ukrida.triemas4018week10.ui.screens


import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import org.ukrida.triemas4018week10.ui.states.ProductUiState

@Composable
fun ProductScreen(
    uiState: ProductUiState,
    onSearchChange: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    onBackToList: () -> Unit,
    onRetry: () -> Unit
) {
    val selectedProduct = uiState.selectedProduct

    BackHandler(enabled = selectedProduct != null) {
        onBackToList()
    }

    if (selectedProduct != null) {
        ProductDetailScreen(
            product = selectedProduct,
            onBack = onBackToList
        )
    } else {
        ProductListScreen(
            products = uiState.products,
            searchText = uiState.searchQuery,
            onSearchChange = onSearchChange,
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            onRetry = onRetry,
            onProductClick = { product ->
                onProductClick(product.productId)
            }
        )
    }
}

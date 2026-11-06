package org.ukrida.triemas4018week10.ui.states

import org.ukrida.triemas4018week10.data.models.Product

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedProduct: Product? = null
)
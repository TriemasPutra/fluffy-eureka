package org.ukrida.triemas4018week10.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ukrida.triemas4018week10.data.models.Product

@Composable
fun ProductListScreen(
    products: List<Product>,
    searchText: String,
    onSearchChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onProductClick: (Product) -> Unit
) {
    val filteredProducts = products.filter {
        it.productName.contains(
            searchText,
            ignoreCase = true
        )
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Products name...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                label = {
                    Text("Search products...")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = androidx.compose.material3
                    .OutlinedTextFieldDefaults.colors(
                        focusedBorderColor =
                            MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor =
                            MaterialTheme.colorScheme.outline,
                        focusedLabelColor =
                            MaterialTheme.colorScheme.primary,
                        cursorColor =
                            MaterialTheme.colorScheme.primary
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))
            when {
                // Loading state
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Loading products...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Error state
                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onRetry,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "Try Again",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
                // Empty state
                filteredProducts.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (products.isEmpty()) {
                                "No products available."
                            } else {
                                "Product not found."
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                // Product list
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredProducts,
                            key = { it.productId }
                        ) { product ->
                            ProductListItem(
                                product = product,
                                onClick = {
                                    onProductClick(product)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

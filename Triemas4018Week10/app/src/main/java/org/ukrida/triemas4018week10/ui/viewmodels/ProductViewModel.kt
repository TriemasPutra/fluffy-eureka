package org.ukrida.triemas4018week10.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ukrida.triemas4018week10.data.repository.ProductRepository
import org.ukrida.triemas4018week10.ui.states.ProductUiState
import kotlin.coroutines.cancellation.CancellationException

class ProductViewModel(private val repository: ProductRepository = ProductRepository()
): ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }
            try {
                val products = repository.getProducts()
                _uiState.update {
                    it.copy(
                        products = products,
                        isLoading = false,
                        selectedProduct = null,
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                    throw e
            } catch (e: ClientRequestException) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage =
                        "Request denied (${e.response.status.value}). " +
                                "Please check your API key and access permissions.")
                }
            } catch (e: ServerResponseException) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage =
                        "A server error occurred (${e.response.status.value}).")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage =
                        "Failed to fetch data. Please check your connection and configuration.")
                }
            }
        }
    }

    fun searchProducts(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun selectProduct(productId: Int) {
        _uiState.update { state ->
            state.copy(
                selectedProduct = state.products.find {
                    it.productId == productId
                }
            )
        }
    }
}
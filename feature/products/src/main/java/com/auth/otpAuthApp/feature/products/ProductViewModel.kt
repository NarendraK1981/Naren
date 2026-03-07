package com.auth.otpAuthApp.feature.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.otpAuthApp.core.domain.GetProductsUseCase
import com.auth.otpAuthApp.core.ui.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _products = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    
    val searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, "")

    val uiState: StateFlow<ProductUiState> = combine(
        _products,
        searchQuery
    ) { state, query ->
        if (state is ProductUiState.Success && query.isNotEmpty()) {
            val filteredProducts = state.products.filter {
                it.name.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true)
            }
            if (filteredProducts.isEmpty()) {
                ProductUiState.Error("No products matching \"$query\"")
            } else {
                ProductUiState.Success(filteredProducts)
            }
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductUiState.Loading
    )

    init {
        fetchProducts()
    }

    fun onSearchQueryChange(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _products.value = ProductUiState.Loading
            delay(1000L)
            val products = getProductsUseCase()

            _products.value = when {
                products.isEmpty() -> {
                    ProductUiState.Error("No products found")
                }
                products.any { !it.inStock } -> {
                    ProductUiState.Error("Not all products in stock")
                }
                else -> {
                    ProductUiState.Success(products)
                }
            }
        }
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query"
    }
}

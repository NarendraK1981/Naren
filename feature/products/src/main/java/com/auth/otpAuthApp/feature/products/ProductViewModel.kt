package com.auth.otpAuthApp.feature.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.otpAuthApp.core.domain.GetProductsUseCase
import com.auth.otpAuthApp.core.domain.model.Product
import com.auth.otpAuthApp.core.ui.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel
@Inject
constructor(
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData<ProductUiState>(ProductUiState.Loading)
    val uiState: LiveData<ProductUiState> = _uiState

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        _uiState.value = ProductUiState.Loading

        viewModelScope.launch {
            delay(1000L)
            val products = getProductsUseCase()

            when {
                products.isEmpty() -> {
                    _uiState.value = ProductUiState.Error("No products found")
                }
                products.any { !it.inStock } -> {
                    _uiState.value = ProductUiState.Error("Not all products in stock")
                }
                else -> {
                    _uiState.value = ProductUiState.Success(products)
                }

            }
        }
    }
}

package com.auth.otpAuthApp.core.ui

import com.auth.otpAuthApp.core.domain.model.Product

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}

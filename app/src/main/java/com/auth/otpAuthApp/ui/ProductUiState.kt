package com.auth.otpAuthApp.ui

import com.auth.otpAuthApp.domain.model.Product

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}

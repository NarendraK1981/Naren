package com.auth.otpAuthApp.core.data.model

data class ApiResponse(
    val status: String,
    val message: String,
    val product: List<ProductDto>,
)

data class ProductDto(
    val id: Int,
    val name: String,
    val price: Double,
    val inStock: Boolean,
)

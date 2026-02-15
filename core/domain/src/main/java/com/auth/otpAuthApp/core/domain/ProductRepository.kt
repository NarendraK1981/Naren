package com.auth.otpAuthApp.core.domain

import com.auth.otpAuthApp.core.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}

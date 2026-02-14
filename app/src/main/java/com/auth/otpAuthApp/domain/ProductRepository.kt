package com.auth.otpAuthApp.domain

import com.auth.otpAuthApp.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}



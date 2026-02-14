package com.auth.otpAuthApp.domain

import com.auth.otpAuthApp.domain.model.Product

class GetProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getProducts()
    }
}

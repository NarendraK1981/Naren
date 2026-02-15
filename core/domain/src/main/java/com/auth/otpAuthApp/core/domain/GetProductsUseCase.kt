package com.auth.otpAuthApp.core.domain

import com.auth.otpAuthApp.core.domain.model.Product

class GetProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): List<Product> = productRepository.getProducts()
}

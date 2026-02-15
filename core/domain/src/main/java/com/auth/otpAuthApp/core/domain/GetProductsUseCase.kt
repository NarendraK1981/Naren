package com.auth.otpAuthApp.core.domain

import com.auth.otpAuthApp.core.domain.model.Product
import jakarta.inject.Inject

class GetProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): List<Product> = productRepository.getProducts()
}

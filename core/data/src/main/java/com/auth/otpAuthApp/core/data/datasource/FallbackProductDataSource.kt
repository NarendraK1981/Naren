package com.auth.otpAuthApp.core.data.datasource

import com.auth.otpAuthApp.core.domain.model.Product
import javax.inject.Inject

class FallbackProductDataSource @Inject constructor() {
    fun getFallbackProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "Product 1",
                price = 10.0,
                inStock = true,
                thumbnail = "",
                description = "",
                rating = 0.0,
                reviews = emptyList(),
                carouselImages = emptyList(),
                minimumOrderQuantity = 1
            ),
            Product(
                id = 2,
                name = "Product 2",
                price = 10.0,
                inStock = true,
                thumbnail = "",
                description = "",
                rating = 0.0,
                reviews = emptyList(),
                carouselImages = emptyList(),
                minimumOrderQuantity = 1
            ),
            Product(
                id = 3,
                name = "Product 3",
                price = 10.0,
                inStock = false,
                thumbnail = "",
                description = "",
                rating = 0.0,
                reviews = emptyList(),
                carouselImages = emptyList(),
                minimumOrderQuantity = 1
            ),
            Product(
                id = 4,
                name = "Product 4",
                price = 10.0,
                inStock = true,
                thumbnail = "",
                description = "",
                rating = 0.0,
                reviews = emptyList(),
                carouselImages = emptyList(),
                minimumOrderQuantity = 1
            ),
        )
    }
}

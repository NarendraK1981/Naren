package com.auth.otpAuthApp.data

import com.auth.otpAuthApp.domain.ProductRepository
import com.auth.otpAuthApp.domain.model.Product
import jakarta.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val apiService: ApiService) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        val response = apiService.getProducts()
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null) {
                return apiResponse.product.map { dto ->
                    Product(
                        id = dto.id,
                        name = dto.name,
                        price = dto.price,
                        inStock = dto.inStock,
                    )
                }
            }
        }
        return listOf(
            Product(
                id = 1,
                name = "Product 1",
                price = 10.0,
                inStock = true,
            ),
            Product(
                id = 2,
                name = "Product 2",
                price = 10.0,
                inStock = true,
            ),
            Product(
                id = 3,
                name = "Product 3",
                price = 10.0,
                inStock = false,
            ),
            Product(
                id = 4,
                name = "Product 4",
                price = 10.0,
                inStock = true,
            ),

        )
    }

}
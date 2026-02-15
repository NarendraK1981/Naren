package com.auth.otpAuthApp.data

import com.auth.otpAuthApp.domain.ProductRepository
import com.auth.otpAuthApp.domain.model.Product
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import timber.log.Timber
import java.io.IOException

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ProductRepository {

    override suspend fun getProducts(): List<Product> = try {
        // Attempt to fetch data with retry logic
        val apiResponse = retryWithBackoff(
            times = 3, // Number of retries
            initialDelay = 1000, // 1 second
            maxDelay = 5000, // Max 5 seconds
        ) {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                response.body()
            } else {
                throw IOException("Server error: ${response.code()}")
            }
        }

        // Map DTOs to Domain models
        apiResponse?.product?.map { dto ->
            Product(
                id = dto.id,
                name = dto.name,
                price = dto.price,
                inStock = dto.inStock,
            )
        } ?: getFallbackProducts()
    } catch (e: Exception) {
        Timber.e(e, "Failed to fetch products after retries")
        getFallbackProducts()
    }

    /**
     * Generic retry function with exponential backoff
     */
    private suspend fun <T> retryWithBackoff(
        times: Int,
        initialDelay: Long,
        maxDelay: Long,
        factor: Double = 2.0,
        block: suspend () -> T,
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                Timber.w("Attempt ${attempt + 1} failed: ${e.message}. Retrying in ${currentDelay}ms...")
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // Last attempt
    }

    private fun getFallbackProducts(): List<Product> = listOf(
        Product(id = 1, name = "Product 1", price = 10.0, inStock = true),
        Product(id = 2, name = "Product 2", price = 10.0, inStock = true),
        Product(id = 3, name = "Product 3", price = 10.0, inStock = false),
        Product(id = 4, name = "Product 4", price = 10.0, inStock = true),
    )
}

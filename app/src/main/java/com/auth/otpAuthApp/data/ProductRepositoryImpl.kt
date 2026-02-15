package com.auth.otpAuthApp.data

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.auth.otpAuthApp.domain.ProductRepository
import com.auth.otpAuthApp.domain.model.Product
import com.auth.otpAuthApp.utils.isNetworkAvailable
import com.auth.otpAuthApp.utils.retryWithBackoff
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import timber.log.Timber
import java.io.IOException

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context,
) : ProductRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getProducts(): List<Product> = try {
        // 1. Initial Check: If no network, skip retries and go straight to fallback or throw
        if (!context.isNetworkAvailable()) {
            Timber.w("No network connection detected. Using fallback.")
            getFallbackProducts()
        } else {
            // 2. Attempt to fetch data with retry logic
            val apiResponse = retryWithBackoff(
                times = 3,
                initialDelay = 1000,
                maxDelay = 5000,
            ) {
                // Double check inside retry in case connection dropped during backoff
                if (!context.isNetworkAvailable()) throw IOException("Network connection lost")

                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Timber.e("Server error: ${response.code()}, ${response.errorBody()?.string()}")
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
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to fetch products after retries")
        getFallbackProducts()
    }

    private fun getFallbackProducts(): List<Product> = listOf(
        Product(id = 1, name = "Product 1", price = 10.0, inStock = true),
        Product(id = 2, name = "Product 2", price = 10.0, inStock = true),
        Product(id = 3, name = "Product 3", price = 10.0, inStock = false),
        Product(id = 4, name = "Product 4", price = 10.0, inStock = true),
    )
}

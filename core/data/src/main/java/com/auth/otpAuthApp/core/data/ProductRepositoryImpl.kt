package com.auth.otpAuthApp.core.data

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.auth.otpAuthApp.core.common.isNetworkAvailable
import com.auth.otpAuthApp.core.common.retryWithBackoff
import com.auth.otpAuthApp.core.domain.ProductRepository
import com.auth.otpAuthApp.core.domain.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import timber.log.Timber
import java.io.IOException

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao,
    @ApplicationContext private val context: Context
) : ProductRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getProducts(): List<Product> = try {
        if (!context.isNetworkAvailable()) {
            Timber.w("No network connection detected. Using local database.")
            val localProducts = productDao.getProducts()
            if (localProducts.isEmpty()) {
                getFallbackProducts()
            } else {
                localProducts.map { entity ->
                    Product(
                        id = entity.id,
                        name = entity.name,
                        price = entity.price,
                        inStock = entity.inStock,
                        thumbnail = entity.thumbnail
                    )
                }
            }
        } else {
            val apiResponse = retryWithBackoff(
                times = 3,
                initialDelay = 1000,
                maxDelay = 5000,
            ) {
                if (!context.isNetworkAvailable()) throw IOException("Network connection lost")

                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw IOException("Server error: ${response.code()}")
                }
            }

            val products = apiResponse?.products?.map { dto ->
                Product(
                    id = dto.id,
                    name = dto.title,
                    price = dto.price,
                    inStock = dto.stock > 0,
                    thumbnail = dto.thumbnail
                )
            } ?: getFallbackProducts()

            // Cache products in Room
            productDao.insertProducts(products.map {
                ProductEntity(
                    id = it.id,
                    name = it.name,
                    price = it.price,
                    inStock = it.inStock,
                    thumbnail = it.thumbnail
                )
            })

            products
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to fetch products after retries")
        getFallbackProducts()
    }


    private suspend fun getFallbackProducts(): List<Product> {
        val products = listOf(
            Product(id = 1, name = "Product 1", price = 10.0, inStock = true, thumbnail = ""),
            Product(id = 2, name = "Product 2", price = 10.0, inStock = true, thumbnail = ""),
            Product(id = 3, name = "Product 3", price = 10.0, inStock = false, thumbnail = ""),
            Product(id = 4, name = "Product 4", price = 10.0, inStock = true, thumbnail = ""),
        )

        productDao.insertProducts(products.map {
            ProductEntity(
                id = it.id,
                name = it.name,
                price = it.price,
                inStock = it.inStock,
                thumbnail = it.thumbnail
            )
        })

        return products
    }
}

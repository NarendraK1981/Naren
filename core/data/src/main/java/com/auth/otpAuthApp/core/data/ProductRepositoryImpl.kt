package com.auth.otpAuthApp.core.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.auth.otpAuthApp.core.common.NetworkMonitor
import com.auth.otpAuthApp.core.common.retryWithBackoff
import com.auth.otpAuthApp.core.data.datasource.FallbackProductDataSource
import com.auth.otpAuthApp.core.data.datasource.LocalProductDataSource
import com.auth.otpAuthApp.core.data.datasource.RemoteProductDataSource
import com.auth.otpAuthApp.core.domain.ProductRepository
import com.auth.otpAuthApp.core.domain.model.Product
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteProductDataSource,
    private val localDataSource: LocalProductDataSource,
    private val fallbackDataSource: FallbackProductDataSource,
    private val networkMonitor: NetworkMonitor
) : ProductRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getProducts(): List<Product> {
        return try {
            if (!networkMonitor.isOnline) {
                Timber.w("No network connection detected. Using local database.")
                localDataSource.getProducts().ifEmpty {
                    handleFallback()
                }
            } else {
                fetchFromRemoteAndCache()
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch products after retries")
            handleFallback()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private suspend fun fetchFromRemoteAndCache(): List<Product> {
        return try {
            val products = retryWithBackoff(
                times = 3,
                initialDelay = 1000,
                maxDelay = 5000,
            ) {
                if (!networkMonitor.isOnline) throw IOException("Network connection lost")
                remoteDataSource.getProducts()
            }

            if (products.isNotEmpty()) {
                localDataSource.saveProducts(products)
                products
            } else {
                handleFallback()
            }
        } catch (e: Exception) {
            Timber.e(e, "Remote fetch failed, trying local storage")
            localDataSource.getProducts().ifEmpty { handleFallback() }
        }
    }

    private suspend fun handleFallback(): List<Product> {
        val fallbackProducts = fallbackDataSource.getFallbackProducts()
        localDataSource.saveProducts(fallbackProducts)
        return fallbackProducts
    }
}

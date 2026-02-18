package com.auth.otpAuthApp.core.data

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.auth.otpAuthApp.core.common.NetworkMonitor
import com.auth.otpAuthApp.core.common.retryWithBackoff
import com.auth.otpAuthApp.core.data.datasource.FallbackProductDataSource
import com.auth.otpAuthApp.core.data.datasource.LocalProductDataSource
import com.auth.otpAuthApp.core.data.datasource.RemoteProductDataSource
import com.auth.otpAuthApp.core.data.datasource.SyncPreferences
import com.auth.otpAuthApp.core.domain.ProductRepository
import com.auth.otpAuthApp.core.domain.model.Product
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteProductDataSource,
    private val localDataSource: LocalProductDataSource,
    private val fallbackDataSource: FallbackProductDataSource,
    private val networkMonitor: NetworkMonitor,
    private val workManager: WorkManager,
    private val syncPreferences: SyncPreferences
) : ProductRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getProducts(): List<Product> {
        val isOnline = networkMonitor.isOnline
        val localData = localDataSource.getProducts()

        return if (isOnline) {
            // If online, fetch fresh data immediately
            fetchFromRemoteAndCache()
        } else {
            // If offline, schedule a sync to happen when network returns
            if (localData.isEmpty()) {
                scheduleSync()
                handleFallback()
            } else {
                // Check if local data is stale (e.g., > 1 hour) before scheduling
                scheduleSync()
                localData
            }
        }
    }

    override fun scheduleSync() {
        val currentTime = System.currentTimeMillis()
        val lastSync = syncPreferences.lastSyncTimestamp
        val tenMinutesInMillis = TimeUnit.MINUTES.toMillis(10)

        if (currentTime - lastSync < tenMinutesInMillis) {
            Timber.d("Sync skipped. Last sync was less than 10 minutes ago.")
            return
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(syncRequest)
        
        // Also schedule periodic sync
        val periodicSyncRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
            
        workManager.enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncRequest
        )

        syncPreferences.lastSyncTimestamp = currentTime
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
                syncPreferences.lastSyncTimestamp = System.currentTimeMillis()
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

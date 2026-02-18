package com.auth.otpAuthApp.core.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.auth.otpAuthApp.core.data.datasource.LocalProductDataSource
import com.auth.otpAuthApp.core.data.datasource.RemoteProductDataSource
import com.auth.otpAuthApp.core.data.datasource.SyncPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val remoteDataSource: RemoteProductDataSource,
    private val localDataSource: LocalProductDataSource,
    private val syncPreferences: SyncPreferences
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Timber.d("Starting background sync for products")
        return try {
            val products = remoteDataSource.getProducts()
            if (products.isNotEmpty()) {
                localDataSource.saveProducts(products)
                syncPreferences.lastSyncTimestamp = System.currentTimeMillis()
                Timber.d("Sync successful")
                Result.success()
            } else {
                Timber.w("Sync returned empty product list")
                Result.retry()
            }
        } catch (e: Exception) {
            Timber.e(e, "Sync failed")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "SyncWorker"
    }
}

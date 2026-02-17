package com.auth.otpAuthApp.core.data.datasource

import com.auth.otpAuthApp.core.data.ApiService
import com.auth.otpAuthApp.core.data.mapper.toDomain
import com.auth.otpAuthApp.core.domain.model.Product
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class RemoteProductDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProducts(): List<Product> {
        val response = apiService.getProducts()
        if (response.isSuccessful) {
            return response.body()?.products?.map { it.toDomain() } ?: emptyList()
        } else {
            throw IOException("Server error: ${response.code()}")
        }
    }
}

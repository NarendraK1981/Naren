package com.auth.otpAuthApp.core.data.datasource

import com.auth.otpAuthApp.core.data.ProductDao
import com.auth.otpAuthApp.core.data.mapper.toDomain
import com.auth.otpAuthApp.core.data.mapper.toEntity
import com.auth.otpAuthApp.core.domain.model.Product
import javax.inject.Inject

class LocalProductDataSource @Inject constructor(
    private val productDao: ProductDao
) {
    suspend fun getProducts(): List<Product> {
        return productDao.getProducts().map { it.toDomain() }
    }

    suspend fun saveProducts(products: List<Product>) {
        productDao.insertProducts(products.map { it.toEntity() })
    }
}

package com.auth.otpAuthApp.core.data

import com.auth.otpAuthApp.core.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>
}

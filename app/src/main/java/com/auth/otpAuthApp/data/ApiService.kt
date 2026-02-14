package com.auth.otpAuthApp.data

import com.auth.otpAuthApp.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("dummy/products")
    suspend fun getProducts(): Response<ApiResponse>
}

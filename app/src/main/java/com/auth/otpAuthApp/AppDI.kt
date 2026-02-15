package com.auth.otpAuthApp

import com.auth.otpAuthApp.data.ApiService
import com.auth.otpAuthApp.data.ProductRepositoryImpl
import com.auth.otpAuthApp.domain.GetProductsUseCase
import com.auth.otpAuthApp.domain.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDI {

    private const val BASE_URL = "http://example.com/api/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideGetProductsUseCase(repositoryModule: ProductRepository): GetProductsUseCase = GetProductsUseCase(repositoryModule)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductRepositoryModule {
    @Binds
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository
}

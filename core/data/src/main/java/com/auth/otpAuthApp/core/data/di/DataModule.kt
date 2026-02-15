package com.auth.otpAuthApp.core.data.di

import com.auth.otpAuthApp.core.data.ApiService
import com.auth.otpAuthApp.core.data.ProductRepositoryImpl
import com.auth.otpAuthApp.core.domain.GetProductsUseCase
import com.auth.otpAuthApp.core.domain.ProductRepository
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
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    companion object {
        @Provides
        @Singleton
        fun provideApiService(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideGetProductsUseCase(repositoryModule: ProductRepository): GetProductsUseCase =
            GetProductsUseCase(repositoryModule)
    }
}



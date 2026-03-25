package com.auth.otpAuthApp.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.auth.otpAuthApp.core.data.ApiService
import com.auth.otpAuthApp.core.data.ProductDao
import com.auth.otpAuthApp.core.data.ProductDatabase
import com.auth.otpAuthApp.core.data.ProductRepositoryImpl
import com.auth.otpAuthApp.core.data.api.OtpApi
import com.auth.otpAuthApp.core.domain.GetProductsUseCase
import com.auth.otpAuthApp.core.domain.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
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
        fun provideOkHttpClient(): OkHttpClient {
            // Certificate Pinning ensures the app only talks to servers with a specific public key
            val certificatePinner = CertificatePinner.Builder()
                .add("dummyjson.com", "sha256/Vjs8r4z+80wjNVKubAYnJHJWVFA6mKcGJS9M69G1C5c=") // Example Hash for dummyjson
                .build()

            return OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build()
        }

        @Provides
        @Singleton
        fun provideApiService(okHttpClient: OkHttpClient): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .client(okHttpClient) // Attach the pinned client here
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideOtpApi(): OtpApi {
            // Node backend runs on port 3443 as per server.js
            return Retrofit.Builder()
                .baseUrl("http://192.168.4.232:3443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OtpApi::class.java)
        }

        @Provides
        @Singleton
        fun provideGetProductsUseCase(repositoryModule: ProductRepository): GetProductsUseCase =
            GetProductsUseCase(repositoryModule)

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): ProductDatabase {
            return Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                "product_db"
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        @Singleton
        fun provideProductDao(database: ProductDatabase): ProductDao {
            return database.productDao()
        }

        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}

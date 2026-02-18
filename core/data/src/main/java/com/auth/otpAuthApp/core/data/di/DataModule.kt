package com.auth.otpAuthApp.core.data.di

import android.content.Context
import androidx.room.Room
import com.auth.otpAuthApp.core.data.ApiService
import com.auth.otpAuthApp.core.data.MIGRATION_1_2
import com.auth.otpAuthApp.core.data.MIGRATION_2_3
import com.auth.otpAuthApp.core.data.ProductDao
import com.auth.otpAuthApp.core.data.ProductDatabase
import com.auth.otpAuthApp.core.data.ProductRepositoryImpl
import com.auth.otpAuthApp.core.domain.GetProductsUseCase
import com.auth.otpAuthApp.core.domain.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): ProductDatabase {
            return Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                "product_db"
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
        }

        @Provides
        @Singleton
        fun provideProductDao(database: ProductDatabase): ProductDao {
            return database.productDao()
        }
    }
}

package com.auth.otpAuthApp.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val inStock: Boolean,
    val thumbnail: String,
    val carouselImages: List<String>
)

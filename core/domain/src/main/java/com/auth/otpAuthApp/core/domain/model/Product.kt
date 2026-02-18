package com.auth.otpAuthApp.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val inStock: Boolean,
    val thumbnail: String,
    val carouselImages: List<String>,
    val rating: Double,
    val reviews: List<Review>,
    val minimumOrderQuantity:Int
)
@Serializable
data class Review(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)

package com.auth.otpAuthApp.core.data.mapper

import com.auth.otpAuthApp.core.common.DateUtils
import com.auth.otpAuthApp.core.data.ProductEntity
import com.auth.otpAuthApp.core.data.Review
import com.auth.otpAuthApp.core.data.model.Product as ProductDto
import com.auth.otpAuthApp.core.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        description = description,
        price = price,
        inStock = stock > 0,
        thumbnail = thumbnail,
        carouselImages = images,
        rating = rating,
        reviews = reviews.map { review ->
            com.auth.otpAuthApp.core.domain.model.Review(
                rating = review.rating,
                comment = review.comment,
                date = DateUtils.formatDate(review.date),
                reviewerName = review.reviewerName,
                reviewerEmail = review.reviewerEmail
            )
        }
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        inStock = inStock,
        thumbnail = thumbnail,
        carouselImages = carouselImages,
        rating = rating,
        reviews = reviews.map { review ->
            com.auth.otpAuthApp.core.domain.model.Review(
                rating = review.rating,
                comment = review.comment,
                date = DateUtils.formatDate(review.date),
                reviewerName = review.reviewerName,
                reviewerEmail = review.reviewerEmail
            )
        }
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        inStock = inStock,
        thumbnail = thumbnail,
        carouselImages = carouselImages,
        rating = rating,
        reviews = reviews.map { review ->
            Review(
                rating = review.rating,
                comment = review.comment,
                date = DateUtils.formatDate(review.date),
                reviewerName = review.reviewerName,
                reviewerEmail = review.reviewerEmail
            )
        }
    )
}

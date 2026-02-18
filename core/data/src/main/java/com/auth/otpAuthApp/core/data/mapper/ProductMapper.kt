package com.auth.otpAuthApp.core.data.mapper

import com.auth.otpAuthApp.core.data.ProductEntity
import com.auth.otpAuthApp.core.data.model.Product as ProductDto
import com.auth.otpAuthApp.core.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        price = price,
        inStock = stock > 0,
        thumbnail = thumbnail,
        carouselImages = images
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        inStock = inStock,
        thumbnail = thumbnail,
        carouselImages = carouselImages
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        price = price,
        inStock = inStock,
        thumbnail = thumbnail,
        carouselImages = carouselImages
    )
}

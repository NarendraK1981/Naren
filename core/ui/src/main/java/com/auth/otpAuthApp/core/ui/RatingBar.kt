package com.auth.otpAuthApp.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starsColor: Color = Color(0xFFFFC107),
    starSize: Dp = 24.dp
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = rating.rem(1) != 0.0

    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starSize)
            )
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Filled.StarHalf,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starSize)
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

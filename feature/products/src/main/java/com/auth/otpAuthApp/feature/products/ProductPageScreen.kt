package com.auth.otpAuthApp.feature.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.auth.otpAuthApp.core.domain.model.Product

@Composable
fun ProductPageScreen(
    sessionExpired: () -> Unit,
    product: Product
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = product.thumbnail,
            contentDescription = product.name
        )
    }


}
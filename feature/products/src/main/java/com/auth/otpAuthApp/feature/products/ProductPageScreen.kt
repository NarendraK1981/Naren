package com.auth.otpAuthApp.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.auth.otpAuthApp.core.domain.model.Product
import com.auth.otpAuthApp.core.ui.CommonTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPageScreen(
    product: Product,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { product.carouselImages.size })

    Scaffold(
        topBar = {
            CommonTopAppBar(title = "", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "$${product.price}", style = MaterialTheme.typography.titleMedium, color = Color.Red)

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    AsyncImage(
                        model = product.carouselImages[page],
                        contentDescription = "Product Image ${page + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
                // Pager Indicator
                if (product.carouselImages.size > 1) {
                    Row(
                        Modifier
                            .height(50.dp)
                            .padding(top = 32.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(product.carouselImages.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            }
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }

                    Text(
                        text = "Image ${pagerState.currentPage + 1} of ${product.carouselImages.size}",
                        color = Color.Black
                    )

                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium, color = Color.Black)

        }
    }
}


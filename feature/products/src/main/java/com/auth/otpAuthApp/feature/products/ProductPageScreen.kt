package com.auth.otpAuthApp.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.auth.otpAuthApp.core.domain.model.Product
import com.auth.otpAuthApp.core.domain.model.Review
import com.auth.otpAuthApp.core.ui.CommonTopAppBar
import com.auth.otpAuthApp.core.ui.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPageScreen(
    product: Product,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { product.carouselImages.size })
    var showReviews by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CommonTopAppBar(title = "", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "$${product.price}", style = MaterialTheme.typography.titleMedium, color = Color.Red)

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { showReviews = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(rating = product.rating, starSize = 16.dp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${product.rating} (${product.reviews.size} reviews)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
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
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(Color.White.copy(alpha = 0.7f)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
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
                            color = Color.Black,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }

    if (showReviews) {
        ModalBottomSheet(
            onDismissRequest = { showReviews = false },
            sheetState = sheetState
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
            ) {
                item {
                    Text(
                        text = "Customer Reviews",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(product.reviews) { review ->
                    ReviewItem(review)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = review.reviewerName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(text = review.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        RatingBar(rating = review.rating.toDouble(), starSize = 12.dp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = review.comment, style = MaterialTheme.typography.bodyMedium)
    }
}

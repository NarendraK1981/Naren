package com.auth.otpAuthApp.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.auth.otpAuthApp.core.domain.model.Product

@Composable
fun ProductCard(product: Product, onProductItemClick: (Product) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        onClick = { onProductItemClick(product) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                       .background(color = Color.White)

        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.name,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF388E3C),
                )

                Text(
                    text = if (product.inStock) "In Stock" else "Out of Stock",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (product.inStock) Color(0xFF2E7D32) else Color.Red,
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductCardPreview() {
    ProductCard(
        product = Product(
            id = 1,
            name = "Product 1",
            price = 10.0,
            inStock = true,
            thumbnail = "https://via.placeholder.com/150",
            carouselImages = emptyList(),
                    description = "",
        ),
        onProductItemClick = {},
    )
}

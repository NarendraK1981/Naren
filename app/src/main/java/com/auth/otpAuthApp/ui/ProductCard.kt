package com.auth.otpAuthApp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.auth.otpAuthApp.domain.model.Product

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
    ) {
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

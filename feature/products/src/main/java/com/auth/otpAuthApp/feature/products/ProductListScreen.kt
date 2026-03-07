package com.auth.otpAuthApp.feature.products

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.otpAuthApp.core.domain.model.Product
import com.auth.otpAuthApp.core.ui.ProductUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    sessionExpired: () -> Unit,
    onProductItemClick: (Product) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val context = LocalContext.current

    BackHandler {
        (context as? Activity)?.finish()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Products") })
        },
        bottomBar = {
            Button(onClick = { sessionExpired() }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Logout")
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState) {
                    is ProductUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is ProductUiState.Error -> {
                        val message = (uiState as ProductUiState.Error).message
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(text = message, color = Color.Red)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { viewModel.fetchProducts() }) {
                                Text("Retry")
                            }
                        }
                    }
                    is ProductUiState.Success -> {
                        val products = (uiState as ProductUiState.Success).products
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                        ) {
                            items(products) { product ->
                                ProductCard(product, onProductItemClick)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
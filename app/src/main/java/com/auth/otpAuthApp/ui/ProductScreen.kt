import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.auth.otpAuthApp.ui.ProductCard
import com.auth.otpAuthApp.ui.ProductUiState
import com.auth.otpAuthApp.viewmodel.ProductViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    sessionExpired: () -> Unit
) {
    val uiState by viewModel.uiState.observeAsState(ProductUiState.Loading)

    val context = LocalContext.current

    // Handle back press to close the app
    BackHandler {
        (context as? Activity)?.finish()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Products") })
        },
        bottomBar = {
            Button(onClick = { sessionExpired() }, modifier = Modifier.fillMaxWidth()) {
                Text("Logout")
            }
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {

                is ProductUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ProductUiState.Error -> {
                    val message = (uiState as ProductUiState.Error).message

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                            .padding(16.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(product)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

            }
        }
    }
}

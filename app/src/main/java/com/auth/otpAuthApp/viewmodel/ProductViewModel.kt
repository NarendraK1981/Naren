package com.auth.otpAuthApp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.otpAuthApp.domain.GetProductsUseCase
import com.auth.otpAuthApp.domain.model.Product
import com.auth.otpAuthApp.ui.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel
@Inject
constructor(
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData<ProductUiState>(ProductUiState.Loading)
    val uiState: LiveData<ProductUiState> = _uiState

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        _uiState.value = ProductUiState.Loading

        viewModelScope.launch {
            delay(1000L)
            val products = getProductsUseCase()
            _uiState.value = ProductUiState.Success(products)
        }
    }
}

package com.example.rest01.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rest01.model.Product
import com.example.rest01.network.ApiService
import com.example.rest01.network.NetworkResult
import com.example.rest01.network.NetworkUtils
import com.example.rest01.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository = ProductRepository(ApiService.create())
    private val _products = MutableLiveData<NetworkResult<List<Product>>>()
    val products: LiveData<NetworkResult<List<Product>>> get() = _products

    init {
        loadProducts()
    }

    fun loadProducts() {
        _products.value = NetworkResult.Loading()

        if (!NetworkUtils.isInternetAvailable(getApplication())) {
            _products.value = NetworkResult.Error("No Internet Connection! Please connect and retry.")
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.fetchProducts()
                if (response.isSuccessful && response.body() != null) {
                    _products.value = NetworkResult.Success(response.body()!!)
                } else {
                    _products.value = NetworkResult.Error("API Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _products.value = NetworkResult.Error("Failed to fetch data: ${e.localizedMessage}")
            }
        }
    }
}
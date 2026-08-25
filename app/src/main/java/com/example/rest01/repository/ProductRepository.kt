package com.example.rest01.repository

import com.example.rest01.model.Product
import com.example.rest01.network.ApiService
import retrofit2.Response

class ProductRepository(private val apiService: ApiService) {
    suspend fun fetchProducts(): Response<List<Product>> {
        return apiService.getProducts()
    }
}
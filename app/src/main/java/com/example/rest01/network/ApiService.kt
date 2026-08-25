package com.example.rest01.network

import com.example.rest01.model.Product
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/products")
    suspend fun getProducts(): Response<List<Product>>

    companion object {
        private const val BASE_URL = "https://api.escuelajs.co/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
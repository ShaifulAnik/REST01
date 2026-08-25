package com.example.rest01

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rest01.adapter.ProductAdapter
import com.example.rest01.databinding.ActivityProductBinding
import com.example.rest01.network.NetworkResult
import com.example.rest01.viewmodel.ProductViewModel

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        observeViewModel()

        // Retry Button Click Handling
        binding.btnRetry.setOnClickListener {
            viewModel.loadProducts()
        }

        // FAB Button Click Handling (Points 11: Refresh products list)
        binding.fabRefresh.setOnClickListener {
            viewModel.loadProducts()
            Toast.makeText(this, "Refreshing products...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList())
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.products.observe(this) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                    binding.recyclerViewProducts.visibility = View.GONE
                }
                is NetworkResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                    binding.recyclerViewProducts.visibility = View.VISIBLE
                    result.data?.let { 
                        adapter.updateData(it)
                        // ডাটা লোড হওয়ার পর একদম শুরু থেকে দেখানোর জন্য
                        binding.recyclerViewProducts.post {
                            binding.recyclerViewProducts.scrollToPosition(0)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewProducts.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvError.text = result.message
                }
            }
        }
    }
}
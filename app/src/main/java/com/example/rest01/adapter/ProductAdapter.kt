package com.example.rest01.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.rest01.databinding.ItemProductRowBinding
import com.example.rest01.model.Product

class ProductAdapter(private var productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ItemProductRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvPrice.text = "$${item.price}"
        holder.binding.tvDescription.text = item.description

        // 3 ImageView Reference List from XML
        val imageViews = listOf(
            holder.binding.imgProduct1,
            holder.binding.imgProduct2,
            holder.binding.imgProduct3
        )

        // API URL Cleanup & Parsing
        val cleanImages = item.images.map { url ->
            url.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .trim()
        }.filter { it.isNotEmpty() }

        // Load images into 3 ImageViews
        for (i in 0 until 3) {
            val imageView = imageViews[i]
            if (i < cleanImages.size) {
                imageView.visibility = View.VISIBLE
                loadImage(imageView, cleanImages[i])
            } else {
                imageView.visibility = View.GONE
            }
        }
    }

    private fun loadImage(imageView: ImageView, url: String) {
        val glideUrl = GlideUrl(
            url,
            LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .build()
        )

        Glide.with(imageView.context)
            .load(glideUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.stat_notify_error)
            .into(imageView)
    }

    override fun getItemCount(): Int = productList.size

    fun updateData(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
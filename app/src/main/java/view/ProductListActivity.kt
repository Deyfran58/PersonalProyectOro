package com.example.goldnet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.productlistactivity)

        val rv = findViewById<RecyclerView>(R.id.rvProducts)
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = ProductAdapter(SplashActivity.SampleData.productController.listAll()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("productId", product.id)
            startActivity(intent)
        }
        rv.adapter = adapter
    }
}

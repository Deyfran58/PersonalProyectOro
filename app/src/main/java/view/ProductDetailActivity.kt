package com.example.goldnet
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.snackbar.Snackbar




class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.productdetailactivity)

        val productId = intent.getStringExtra("productId") ?: return
        val product = SplashActivity.SampleData.productController.findById(productId) ?: return

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvKarat = findViewById<TextView>(R.id.tvKarat)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val btnBuy = findViewById<Button>(R.id.btnBuyNow)

        tvTitle.text = product.title
        tvKarat.text = "${getString(R.string.karat)}: ${product.karat}"
        tvPrice.text = product.price.toString()

        btnBuy.setOnClickListener {
            // For demo: get first sample user
            val user = SplashActivity.SampleData.userController.userManager.getAll().firstOrNull() // you may expose helper
            if (user != null) {
                SplashActivity.SampleData.purchaseController.createPurchase(user.id, product.id, 1)
                Snackbar.make(btnBuy, getString(R.string.success_purchase), Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(btnBuy, "No user logged in", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

private fun Unit.firstOrNull() {
    TODO("Not yet implemented")
}

private fun Any.getAll() {
    TODO("Not yet implemented")
}

private fun Any.createPurchase(id: Any, id2: Any, i: Int) {
    TODO("Not yet implemented")
}

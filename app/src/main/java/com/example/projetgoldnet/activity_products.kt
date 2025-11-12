package com.example.projetgoldnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

// CLASS FOR PRODUCTS
data class CartItem(val name: String, val price: Int) : java.io.Serializable

class ProductsActivity : AppCompatActivity() {

    // GLOBAL CART (SHARED)
    companion object {
        val cartItems = mutableListOf<CartItem>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_products)

        // Padding for bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // MENU BUTTON → DASHBOARD
        findViewById<MaterialButton>(R.id.btnMenu).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }


    }

    private fun addToCart(name: String, price: Int) {
        cartItems.add(CartItem(name, price))
        Toast.makeText(this, "$name agregado al carrito!", Toast.LENGTH_SHORT).show()
    }
}
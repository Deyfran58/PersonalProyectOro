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

        findViewById<MaterialButton>(R.id.btnMenu).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        // RED BUTTON 1: Add Cadena Real 10K and go to Cart
        findViewById<MaterialButton>(R.id.btnAdd1).setOnClickListener {
            addToCartAndGo("Cadena Real 10K", 120000)
        }

        // RED BUTTON 2: Add Cadena Imperial 18K and go to Cart
        findViewById<MaterialButton>(R.id.btnAdd2).setOnClickListener {
            addToCartAndGo("Cadena Imperial 18K", 250000)
        }

        // RED BUTTON 3: Add Cadena Sol Dorado 18K and go to Cart
        findViewById<MaterialButton>(R.id.btnAdd3).setOnClickListener {
            addToCartAndGo("Cadena Sol Dorado 18K", 275000)
        }
    }

    // Add to cart and navigate to Cart_Activity
    private fun addToCartAndGo(name: String, price: Int) {
        cartItems.add(CartItem(name, price))
        Toast.makeText(this, "$name ¡Añadido al carrito!", Toast.LENGTH_SHORT).show()

        // Go to Cart screen
        startActivity(Intent(this, Cart_Activity::class.java))
    }
}
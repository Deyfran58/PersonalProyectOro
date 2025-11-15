package com.example.projetgoldnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetgoldnet.databinding.ActivityCartBinding

class Cart_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter
    private val cartItems = ProductsActivity.cartItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        updateTotal()

        binding.btnMenu.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }

        binding.btnPay.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            } else {
                cartItems.clear()
                adapter.notifyDataSetChanged()
                updateTotal()

                startActivity(Intent(this, SuccessActivity::class.java))

                finish()
            }
        }

        binding.btnClearCart.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "El carrito ya está vacío", Toast.LENGTH_SHORT).show()
            } else {
                cartItems.clear()
                adapter.notifyDataSetChanged()
                updateTotal()
                Toast.makeText(this, "Carrito vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(cartItems)
        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(this@Cart_Activity)
            adapter = this@Cart_Activity.adapter
        }
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price }
        binding.tvTotal.text = "Total: ₡${String.format("%,d", total)}"
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        updateTotal()
    }
}
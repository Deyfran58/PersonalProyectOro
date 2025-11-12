package com.example.projetgoldnet

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // DISPLAY USERNAME
        val username = intent.getStringExtra("USERNAME") ?: "Usuario"
        findViewById<TextView>(R.id.tvWelcome).text = "¡Bienvenido, $username!"

        // BUTTON: PRODUCTS
        findViewById<MaterialButton>(R.id.btnMisPedidos).setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }

        // BUTTON: REVIEWS
        findViewById<MaterialButton>(R.id.btnResenas).setOnClickListener {
            startActivity(Intent(this, ReviewsActivity::class.java))
        }

        // BUTTON: MY CART
        findViewById<MaterialButton>(R.id.btnNotificaciones).setOnClickListener {
             startActivity(Intent(this, Cart_Activity::class.java))
         }

        // BUTTON: LOG OUT
        findViewById<MaterialButton>(R.id.btnCerrarSesion).setOnClickListener {
            Toast.makeText(this, "¡Sesión cerrada!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
package com.example.goldnet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar




class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegisterLink)

        btnLogin.setOnClickListener {
            val user = SplashActivity.SampleData.userController.login(etEmail.text.toString(), etPassword.text.toString())
            if (user != null) {
                startActivity(Intent(this, ProductListActivity::class.java))
                finish()
            } else {
                Snackbar.make(btnLogin, getString(R.string.invalid_credentials), Snackbar.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}

private fun Any.login(toString: String, toString2: String) {
    TODO("Not yet implemented")
}

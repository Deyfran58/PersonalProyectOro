package com.example.goldnet


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirm = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            if (etPassword.text.toString() != etConfirm.text.toString()) {
                Snackbar.make(btnRegister, "Passwords do not match", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = SampleData.userController.register(etName.text.toString(), etEmail.text.toString(), etPassword.text.toString())
            Snackbar.make(btnRegister, getString(R.string.registration_success), Snackbar.LENGTH_SHORT).show()
            finish()
        }
    }
}

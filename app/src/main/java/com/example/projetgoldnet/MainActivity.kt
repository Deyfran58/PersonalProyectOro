package com.example.projetgoldnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCedula = findViewById<TextInputEditText>(R.id.etNombre)        // ← sigue siendo el mismo ID
        val etContrasena = findViewById<TextInputEditText>(R.id.etContrasena)
        val btnLogin = findViewById<MaterialButton>(R.id.btnIniciarSesion)
        val btnRegister = findViewById<MaterialButton>(R.id.btnCrearCuenta)

        btnLogin.setOnClickListener {
            val cedula = etCedula.text.toString().trim()
            val contrasena = etContrasena.text.toString()

            when {
                cedula.isEmpty() || contrasena.isEmpty() -> {
                    toast("Complete cédula y contraseña")
                    return@setOnClickListener
                }
                else -> hacerLogin(cedula, contrasena)
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun hacerLogin(cedula: String, contrasena: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUser(cedula)
                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()!!.user!!
                    if (user.contrasena == contrasena) {
                        toast("¡Bienvenido ${user.nombre} ${user.primerApellido}!")
                        val intent = Intent(this@MainActivity, DashboardActivity::class.java).apply {
                            putExtra("USERNAME", "${user.nombre} ${user.primerApellido}")
                            putExtra("USER_ID", user.cedula)
                            putExtra("USER_PHOTO", user.fotoPerfil) // para mostrar foto en dashboard
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        toast("Contraseña incorrecta")
                    }
                } else {
                    toast("Cédula no registrada")
                }
            } catch (e: Exception) {
                toast("Error de conexión")
            }
        }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
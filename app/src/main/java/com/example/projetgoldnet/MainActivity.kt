package com.example.projetgoldnet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCedula = findViewById<TextInputEditText>(R.id.etNombre)        // ← Ahora es CÉDULA
        val etContrasena = findViewById<TextInputEditText>(R.id.etContrasena)
        val btnLogin = findViewById<MaterialButton>(R.id.btnIniciarSesion)
        val btnRegister = findViewById<MaterialButton>(R.id.btnCrearCuenta)


        btnLogin.setOnClickListener {
            val cedula = etCedula.text.toString().trim()
            val pass = etContrasena.text.toString()

            when {
                cedula.isEmpty() || pass.isEmpty() -> {
                    toast("Completa cédula y contraseña")
                }
                pass.length < 6 -> {
                    toast("Contraseña muy corta (mín. 6 caracteres)")
                }
                else -> {
                    val prefs = getSharedPreferences("users", Context.MODE_PRIVATE)
                    val userData = prefs.getString(cedula, null)

                    if (userData == null) {
                        toast("Cédula no registrada")
                        return@setOnClickListener
                    }


                    val partes = userData.split("|")
                    val contrasenaGuardada = partes.last()
                    val nombre = partes[0]
                    val primerApellido = partes[1]

                    if (pass == contrasenaGuardada) {

                        val intent = Intent(this, DashboardActivity::class.java).apply {
                            putExtra("USERNAME", "$nombre $primerApellido")
                            putExtra("USER_ID", cedula)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        toast("Contraseña incorrecta")
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
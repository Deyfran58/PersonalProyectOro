package com.example.projetgoldnet

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.util.*

class RegisterActivity : AppCompatActivity() {


    private lateinit var etId: EditText
    private lateinit var etNombre: EditText
    private lateinit var etPrimerApellido: EditText
    private lateinit var etSegundoApellido: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etProvincia: EditText
    private lateinit var etCanton: EditText
    private lateinit var etDistrito: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etConfirmarContrasena: EditText

    // === BOTONES SUPERIORES ===
    private lateinit var btnSave: MaterialButton
    private lateinit var btnUpdate: MaterialButton
    private lateinit var btnDelete: MaterialButton
    private lateinit var tvTitle: android.widget.TextView

    // === ESTADO ===
    private var isEditMode = false
    private var currentUserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        setupToolbar()
        setupListeners()

        // === EDIT MODE FROM DASHBOARD ===
        intent.getStringExtra("EDIT_USER_ID")?.let { id ->
            etId.setText(id)
            searchUser(id)
        }
    }

    private fun setupViews() {
        etId = findViewById(R.id.etId)
        etNombre = findViewById(R.id.etNombre)
        etPrimerApellido = findViewById(R.id.etPrimerApellido)
        etSegundoApellido = findViewById(R.id.etSegundoApellido)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etProvincia = findViewById(R.id.etProvincia)
        etCanton = findViewById(R.id.etCanton)
        etDistrito = findViewById(R.id.etDistrito)
        etDireccion = findViewById(R.id.etDireccion)
        etContrasena = findViewById(R.id.etContrasena)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)

        btnSave = findViewById(R.id.btnSave)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        tvTitle = findViewById(R.id.tvTitle)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.btnCalendar).setOnClickListener { showDatePicker() }
        etFechaNacimiento.setOnClickListener { showDatePicker() }

        // === LUPA BUSCA AL TOCAR ===
        findViewById<ImageButton>(R.id.btnSearch).setOnClickListener {
            val id = etId.text.toString().trim()
            if (id.isEmpty()) {
                toast("Ingresa una cédula para buscar")
            } else {
                searchUser(id)
            }
        }

        btnSave.setOnClickListener { createUser() }
        btnUpdate.setOnClickListener { showUpdateDialog() }
        btnDelete.setOnClickListener { showDeleteDialog() }
    }

    private fun createUser() {
        if (!validateFields()) return
        val id = etId.text.toString().trim()

        if (prefs.contains(id)) {
            toast("Esta cédula ya está registrada")
            return
        }

        saveUser(id)
        toast("¡Cuenta creada exitosamente!")
        goToDashboard()
    }

    private fun searchUser(id: String) {
        if (id.isEmpty()) {
            toast("Ingresa una cédula para buscar")
            return
        }

        if (!prefs.contains(id)) {
            toast("Usuario no encontrado")
            clearFields()
            setEditMode(false)
            return
        }

        val data = prefs.getString(id, "")!!.split("|")
        if (data.size < 11) {
            toast("Datos incompletos. Registra de nuevo.")
            return
        }

        etNombre.setText(data[0])
        etPrimerApellido.setText(data[1])
        etSegundoApellido.setText(data[2])
        etFechaNacimiento.setText(data[3])
        etCorreo.setText(data[4])
        etTelefono.setText(data[5])
        etProvincia.setText(data[6])
        etCanton.setText(data[7])
        etDistrito.setText(data[8])
        etDireccion.setText(data[9])
        etContrasena.setText(data[10])
        etConfirmarContrasena.setText(data[10])

        currentUserId = id
        setEditMode(true)
        toast("Usuario encontrado. Edita y actualiza.")
    }

    private fun updateUser() {
        if (!validateFields()) return
        saveUser(currentUserId)
        toast("¡Cuenta actualizada exitosamente!")
        goToDashboard()
    }

    private fun deleteUser() {
        prefs.edit().remove(currentUserId).apply()
        toast("Cuenta eliminada")
        clearFields()
        setEditMode(false)
    }

    private fun showUpdateDialog() {
        AlertDialog.Builder(this)
            .setTitle("Actualizar Cuenta")
            .setMessage("¿Deseas guardar los cambios?")
            .setPositiveButton("Sí") { _, _ -> updateUser() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Cuenta")
            .setMessage("¿Estás seguro de eliminar esta cuenta permanentemente?")
            .setPositiveButton("Eliminar") { _, _ -> deleteUser() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private val prefs get() = getSharedPreferences("users", Context.MODE_PRIVATE)

    private fun saveUser(id: String) {
        val data = listOf(
            etNombre.text.toString(),
            etPrimerApellido.text.toString(),
            etSegundoApellido.text.toString(),
            etFechaNacimiento.text.toString(),
            etCorreo.text.toString(),
            etTelefono.text.toString(),
            etProvincia.text.toString(),
            etCanton.text.toString(),
            etDistrito.text.toString(),
            etDireccion.text.toString(),
            etContrasena.text.toString()
        ).joinToString("|")

        prefs.edit().putString(id, data).apply()
    }

    private fun validateFields(): Boolean {
        val id = etId.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val pApellido = etPrimerApellido.text.toString().trim()
        val sApellido = etSegundoApellido.text.toString().trim()
        val fecha = etFechaNacimiento.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val provincia = etProvincia.text.toString().trim()
        val canton = etCanton.text.toString().trim()
        val distrito = etDistrito.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()
        val contrasena = etContrasena.text.toString()
        val confirmar = etConfirmarContrasena.text.toString()

        return when {
            id.isEmpty() -> { toast("Ingresa tu cédula"); false }
            nombre.isEmpty() -> { toast("Ingresa tu nombre"); false }
            pApellido.isEmpty() -> { toast("Ingresa primer apellido"); false }
            sApellido.isEmpty() -> { toast("Ingresa segundo apellido"); false }
            fecha.isEmpty() || fecha == "dd/mm/aaaa" -> { toast("Selecciona fecha de nacimiento"); false }
            correo.isEmpty() -> { toast("Ingresa tu correo"); false }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> { toast("Correo inválido"); false }
            telefono.length != 8 || !telefono.all { it.isDigit() } -> { toast("Teléfono debe tener 8 dígitos"); false }
            provincia.isEmpty() -> { toast("Ingresa provincia"); false }
            canton.isEmpty() -> { toast("Ingresa cantón"); false }
            distrito.isEmpty() -> { toast("Ingresa distrito"); false }
            direccion.isEmpty() -> { toast("Ingresa dirección"); false }
            contrasena.length < 6 -> { toast("Contraseña muy corta (mín. 6)"); false }
            contrasena != confirmar -> { toast("Las contraseñas no coinciden"); false }
            else -> true
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            etFechaNacimiento.setText(String.format("%02d/%02d/%d", d, m + 1, y))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun setEditMode(enabled: Boolean) {
        isEditMode = enabled
        btnSave.visibility = if (enabled) View.GONE else View.VISIBLE
        btnUpdate.visibility = if (enabled) View.VISIBLE else View.GONE
        btnDelete.visibility = if (enabled) View.VISIBLE else View.GONE
        tvTitle.text = if (enabled) "Editar Cuenta" else "Crear Cuenta"
    }

    private fun clearFields() {
        listOf(
            etId, etNombre, etPrimerApellido, etSegundoApellido,
            etCorreo, etTelefono, etProvincia, etCanton,
            etDistrito, etDireccion, etContrasena, etConfirmarContrasena
        ).forEach { it.text.clear() }
        etFechaNacimiento.hint = "dd/mm/aaaa"
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java).apply {
            putExtra("USERNAME", "${etNombre.text} ${etPrimerApellido.text}")
            putExtra("USER_ID", etId.text.toString().trim())
        })
        finish()
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
package com.example.projetgoldnet

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class RegisterActivity : AppCompatActivity() {

    // === VIEWS ===
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
    private lateinit var ivProfilePhoto: CircleImageView
    private lateinit var btnAddPhoto: ImageButton
    private lateinit var btnSave: MaterialButton
    private lateinit var btnUpdate: MaterialButton
    private lateinit var btnDelete: MaterialButton
    private lateinit var tvTitle: android.widget.TextView

    private var isEditMode = false
    private var currentUserId = ""
    private var currentBitmap: Bitmap? = null
    private var photoUri: Uri = Uri.EMPTY

    // === LAUNCHERS ===
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { loadImageFromUri(it) }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(photoUri))
            currentBitmap = bitmap
            ivProfilePhoto.setImageBitmap(bitmap)
            saveToGallery(photoUri)
            toast("¡Foto guardada en galería!")
        } else {
            toast("Error al tomar la foto")
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) openCamera() else toast("Permiso de cámara denegado")
    }

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
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        btnSave = findViewById(R.id.btnSave)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        tvTitle = findViewById(R.id.tvTitle)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.btnCalendar).setOnClickListener { showDatePicker() }
        etFechaNacimiento.setOnClickListener { showDatePicker() }

        findViewById<ImageButton>(R.id.btnSearch).setOnClickListener {
            val id = etId.text.toString().trim()
            if (id.isEmpty()) toast("Ingresa una cédula") else searchUser(id)
        }

        btnAddPhoto.setOnClickListener { showImagePickerDialog() }

        btnSave.setOnClickListener { createUser() }
        btnUpdate.setOnClickListener { showUpdateDialog() }
        btnDelete.setOnClickListener { showDeleteDialog() }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Cámara", "Galería")
        AlertDialog.Builder(this)
            .setTitle("Seleccionar foto")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> galleryLauncher.launch("image/*")
                }
            }
            .show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val photoFile = File.createTempFile("photo_${System.currentTimeMillis()}", ".jpg", externalCacheDir)
            photoUri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)
            cameraLauncher.launch(photoUri)
        } else {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun saveToGallery(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "GoldNet_User_${System.currentTimeMillis()}", "Foto de perfil")
    }

    private fun loadImageFromUri(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            currentBitmap = bitmap
            ivProfilePhoto.setImageBitmap(bitmap)
        } catch (e: Exception) {
            toast("Error al cargar imagen")
        }
    }

    // === CRUD CON RETROFIT ===
    private fun createUser() {
        if (!validateFields()) return

        val imageBase64 = if (currentBitmap != null) {
            val stream = ByteArrayOutputStream()
            currentBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)
        } else ""

        val user = User(
            cedula = etId.text.toString().trim(),
            nombre = etNombre.text.toString().trim(),
            primerApellido = etPrimerApellido.text.toString().trim(),
            segundoApellido = etSegundoApellido.text.toString().trim(),
            fechaNacimiento = etFechaNacimiento.text.toString().trim(),
            correo = etCorreo.text.toString().trim(),
            telefono = etTelefono.text.toString().trim(),
            provincia = etProvincia.text.toString().trim(),
            canton = etCanton.text.toString().trim(),
            distrito = etDistrito.text.toString().trim(),
            direccion = etDireccion.text.toString().trim(),
            contrasena = etContrasena.text.toString(),
            fotoPerfil = imageBase64
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.registerUser(user)
                if (response.isSuccessful && response.body()?.success == true) {
                    toast("¡Cuenta creada exitosamente!")
                    goToDashboard()
                } else {
                    toast(response.body()?.message ?: "Esta cédula ya está registrada")
                }
            } catch (e: Exception) {
                toast("Error de conexión: ${e.message}")
            }
        }
    }

    private fun searchUser(cedula: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUser(cedula)
                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()!!.user!!

                    etNombre.setText(user.nombre)
                    etPrimerApellido.setText(user.primerApellido)
                    etSegundoApellido.setText(user.segundoApellido)
                    etFechaNacimiento.setText(user.fechaNacimiento)
                    etCorreo.setText(user.correo)
                    etTelefono.setText(user.telefono)
                    etProvincia.setText(user.provincia)
                    etCanton.setText(user.canton)
                    etDistrito.setText(user.distrito)
                    etDireccion.setText(user.direccion)
                    etContrasena.setText(user.contrasena)
                    etConfirmarContrasena.setText(user.contrasena)

                    // CARGAR FOTO
                    if (user.fotoPerfil.isNotEmpty()) {
                        val imageBytes = android.util.Base64.decode(user.fotoPerfil, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        ivProfilePhoto.setImageBitmap(bitmap)
                        currentBitmap = bitmap
                    } else {
                        ivProfilePhoto.setImageResource(R.drawable.ic_profile_placeholder)
                        currentBitmap = null
                    }

                    currentUserId = cedula
                    setEditMode(true)
                    toast("Usuario encontrado")
                } else {
                    toast("Usuario no encontrado")
                    clearFields()
                    setEditMode(false)
                }
            } catch (e: Exception) {
                toast("Error de conexión")
            }
        }
    }

    private fun updateUser() {
        if (!validateFields()) return

        val imageBase64 = if (currentBitmap != null) {
            val stream = ByteArrayOutputStream()
            currentBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)
        } else ""

        val user = User(
            cedula = currentUserId,
            nombre = etNombre.text.toString().trim(),
            primerApellido = etPrimerApellido.text.toString().trim(),
            segundoApellido = etSegundoApellido.text.toString().trim(),
            fechaNacimiento = etFechaNacimiento.text.toString().trim(),
            correo = etCorreo.text.toString().trim(),
            telefono = etTelefono.text.toString().trim(),
            provincia = etProvincia.text.toString().trim(),
            canton = etCanton.text.toString().trim(),
            distrito = etDistrito.text.toString().trim(),
            direccion = etDireccion.text.toString().trim(),
            contrasena = etContrasena.text.toString(),
            fotoPerfil = imageBase64
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.updateUser(currentUserId, user)
                if (response.isSuccessful && response.body()?.success == true) {
                    toast("¡Cuenta actualizada exitosamente!")
                    goToDashboard()
                } else {
                    toast("Error al actualizar")
                }
            } catch (e: Exception) {
                toast("Error de conexión")
            }
        }
    }

    private fun deleteUser() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.deleteUser(currentUserId)
                if (response.isSuccessful && response.body()?.success == true) {
                    toast("Cuenta eliminada")
                    clearFields()
                    setEditMode(false)
                }
            } catch (e: Exception) {
                toast("Error al eliminar")
            }
        }
    }

    private fun showUpdateDialog() {
        AlertDialog.Builder(this)
            .setTitle("Actualizar")
            .setMessage("¿Guardar cambios?")
            .setPositiveButton("Sí") { _, _ -> updateUser() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar permanentemente?")
            .setPositiveButton("Sí") { _, _ -> deleteUser() }
            .setNegativeButton("No", null)
            .show()
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
            id.isEmpty() -> { toast("Ingresa cédula"); false }
            nombre.isEmpty() -> { toast("Ingresa nombre"); false }
            pApellido.isEmpty() -> { toast("Ingresa primer apellido"); false }
            sApellido.isEmpty() -> { toast("Ingresa segundo apellido"); false }
            fecha.isEmpty() || fecha == "dd/mm/aaaa" -> { toast("Selecciona fecha"); false }
            correo.isEmpty() -> { toast("Ingresa correo"); false }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> { toast("Correo inválido"); false }
            telefono.length != 8 || !telefono.all { it.isDigit() } -> { toast("Teléfono: 8 dígitos"); false }
            provincia.isEmpty() -> { toast("Ingresa provincia"); false }
            canton.isEmpty() -> { toast("Ingresa cantón"); false }
            distrito.isEmpty() -> { toast("Ingresa distrito"); false }
            direccion.isEmpty() -> { toast("Ingresa dirección"); false }
            contrasena.length < 6 -> { toast("Contraseña mínima 6 caracteres"); false }
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
            etId, etNombre, etPrimerApellido, etSegundoApellido, etCorreo, etTelefono,
            etProvincia, etCanton, etDistrito, etDireccion, etContrasena, etConfirmarContrasena
        ).forEach { it.text.clear() }
        etFechaNacimiento.hint = "dd/mm/aaaa"
        ivProfilePhoto.setImageResource(R.drawable.ic_profile_placeholder)
        currentBitmap = null
        photoUri = Uri.EMPTY
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java).apply {
            putExtra("USERNAME", "${etNombre.text} ${etPrimerApellido.text}")
            putExtra("USER_ID", etId.text.toString().trim())
        })
        finish()
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
package com.example.projetgoldnet

data class User(
    val cedula: String,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val fechaNacimiento: String,
    val correo: String,
    val telefono: String,
    val provincia: String,
    val canton: String,
    val distrito: String,
    val direccion: String,
    val contrasena: String,
    val fotoPerfil: String = ""  // Base64 de la imagen
)
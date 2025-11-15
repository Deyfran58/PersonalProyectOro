package com.example.projetgoldnet

import android.graphics.Bitmap

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val photoBitmap: Bitmap?
)
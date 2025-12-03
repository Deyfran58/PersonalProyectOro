package com.example.projetgoldnet

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/users")
    suspend fun registerUser(@Body user: User): Response<ApiResponse>

    @GET("api/users/{cedula}")
    suspend fun getUser(@Path("cedula") cedula: String): Response<ApiResponse>

    @PUT("api/users/{cedula}")
    suspend fun updateUser(@Path("cedula") cedula: String, @Body user: User): Response<ApiResponse>

    @DELETE("api/users/{cedula}")
    suspend fun deleteUser(@Path("cedula") cedula: String): Response<ApiResponse>

    @GET("api/users")
    suspend fun getAllUsers(): Response<ApiResponse>
}
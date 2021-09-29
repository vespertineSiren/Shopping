package com.example.shopapp.api

import com.example.shopapp.models.LoginResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthApi {

    @GET("status")
    suspend fun getToken(@Header("Authorization") credentials: String): Response<LoginResponse>
}

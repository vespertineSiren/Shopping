package com.example.shopapp.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Int,
    @SerializedName("token")
    val token: String?
)

package com.example.shopapp.models

import com.google.gson.annotations.SerializedName

data class Colors(
    @SerializedName("colors")
    val colors: List<String>
)

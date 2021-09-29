package com.example.shopapp.models

import com.google.gson.annotations.SerializedName

data class Styles(
    @SerializedName("styles")
    val styles: List<String>
)

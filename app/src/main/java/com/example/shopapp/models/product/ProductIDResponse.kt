package com.example.shopapp.models.product

import com.google.gson.annotations.SerializedName

data class ProductIDResponse(
    @SerializedName("product")
    val product: Product
)

package com.example.shopapp.models.product

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("product_id")
    val productId: Int
)

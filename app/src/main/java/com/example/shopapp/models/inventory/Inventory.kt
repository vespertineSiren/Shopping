package com.example.shopapp.models.inventory

import com.google.gson.annotations.SerializedName

data class Inventory(
    @SerializedName("color")
    val color: String,
    @SerializedName("cost_cents")
    val costCents: Int,
    @SerializedName("height")
    val height: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("length")
    val length: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("price_cents")
    val priceCents: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("sale_price_cents")
    val salePriceCents: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("sku")
    val sku: String,
    @SerializedName("weight")
    val weight: String,
    @SerializedName("width")
    val width: String
)

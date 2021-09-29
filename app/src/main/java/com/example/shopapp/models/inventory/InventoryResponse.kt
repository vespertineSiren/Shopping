package com.example.shopapp.models.inventory

import com.google.gson.annotations.SerializedName

data class InventoryResponse(
    @SerializedName("inventory_id")
    val inventoryId: Int,
    @SerializedName("message")
    val message: String
)

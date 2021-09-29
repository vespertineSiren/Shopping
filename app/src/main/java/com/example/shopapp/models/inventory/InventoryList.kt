package com.example.shopapp.models.inventory

import com.example.shopapp.models.Size
import com.google.gson.annotations.SerializedName

data class InventoryList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("inventory")
    val inventory: List<Inventory>,
    @SerializedName("total")
    val total: Int
) {

    companion object {
        fun createQueryMap(
            page: String = "0",
            limit: String = "50",
            color: String? = null,
            size: Size? = null,
            quantity: String? = null
        ): Map<String, String> =
            hashMapOf(
                "page" to page,
                "limit" to limit
            ).apply {
                color?.let { safeColor -> put("color", safeColor) }
                size?.let { safeSize -> put("size", safeSize.name) }
                quantity?.let { safeQuantity -> put("quantity", safeQuantity) }
            }
    }
}

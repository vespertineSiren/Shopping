package com.example.shopapp.models.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("admin_id")
    val adminId: Int = 0,
    @SerializedName("brand")
    var brand: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("note")
    val note: String = "",
    @SerializedName("product_name")
    var productName: String = "",
    @SerializedName("product_type")
    val productType: String = "",
    @SerializedName("shipping_price")
    var shippingPrice: Int = 0,
    @SerializedName("style")
    var style: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("url")
    val url: String = ""
) {

    companion object {
        fun createQueryMap(
            name: String? = null,
            description: String? = null,
            style: String? = null,
            brand: String? = null,
            shippingPrice: String? = null
        ): Map<String, String> =
            hashMapOf<String, String>().apply {
                name?.let { safeName -> put("name", safeName) }
                description?.let { safeDesc -> put("description", safeDesc) }
                style?.let { safeStyle -> put("style", safeStyle) }
                brand?.let { safeBrand -> put("brand", safeBrand) }
                shippingPrice?.let { safeCents -> put("shipping_price_cents", safeCents) }
            }
    }
}

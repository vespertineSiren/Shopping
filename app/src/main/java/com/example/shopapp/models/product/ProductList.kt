package com.example.shopapp.models.product

import com.google.gson.annotations.SerializedName

data class ProductList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("total")
    val total: Int
) {

    //  TODO: Improve logic that determines the amount of pages based on the total .
    val pageTotal: Int
        get() = if ((total % count) == 0) total / count else ((total / count) + 1)

    companion object {
        fun createQueryMap(page: Int = 0, limit: Int = 50): Map<String, String> =
            mapOf(
                "page" to page.toString(),
                "limit" to limit.toString()
            )
    }
}

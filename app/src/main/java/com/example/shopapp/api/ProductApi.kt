package com.example.shopapp.api

import com.example.shopapp.models.Colors
import com.example.shopapp.models.Styles
import com.example.shopapp.models.product.Product
import com.example.shopapp.models.product.ProductIDResponse
import com.example.shopapp.models.product.ProductList
import retrofit2.http.*

interface ProductApi {

    @GET("products")
    suspend fun getProductResponse(
        @Header("Authorization") authorization: String
    ): ProductList

    @GET("product/{id}")
    suspend fun getProductById(
        @Header("Authorization") authorization: String,
        @Path("id") productID: Int,
    ): ProductIDResponse

    @GET("products")
    suspend fun getProductResponseByQuery(
        @Header("Authorization") authorization: String,
        @QueryMap queryMap: Map<String, String>
    ): ProductList

    @GET("styles")
    suspend fun getProductStyles(
        @Header("Authorization") authorization: String
    ): Styles

    @GET("colors")
    suspend fun getProductColors(
        @Header("Authorization") authorization: String
    ): Colors

    @POST("product")
    suspend fun createProduct(
        @Header("Authorization") authorization: String,
        @Body product: Product
    ): ProductList

    @PUT("product/{id}")
    suspend fun updateProduct(
        @Header("Authorization") authorization: String,
        @Body product: Product,
        @Path("id") productID: Int,
    ): ProductList

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") authorization: String,
        @Path("id") productID: Int,
    ): ProductList

    @GET("inventory")
    suspend fun getInventoryResponse(
        @Header("Authorization") authorization: String
    ): ProductList
}

package com.example.shopapp.api

import com.example.shopapp.models.inventory.Inventory
import com.example.shopapp.models.inventory.InventoryList
import com.example.shopapp.models.inventory.InventoryResponse
import retrofit2.http.*

interface InventoryApi {

    @GET("inventory")
    suspend fun getInventoryList(
        @Header("Authorization") authorization: String
    ): InventoryList

    @GET("inventory")
    suspend fun getInventoryListByQuery(
        @Header("Authorization") authorization: String,
        @QueryMap queryMap: Map<String, Int>
    ): InventoryList

    @GET("inventory/{id}")
    suspend fun getInventoryItemById(
        @Header("Authorization") authorization: String,
        @Path("id") inventoryID: Int,
    ): Inventory

    @POST("inventory")
    suspend fun createInventoryItem(
        @Header("Authorization") authorization: String,
        @Body inventory: Inventory
    ): InventoryResponse

    @PUT("inventory/{id}")
    suspend fun updateInventoryItem(
        @Header("Authorization") authorization: String,
        @Body inventory: Inventory,
        @Path("id") inventoryID: Int,
    ): InventoryResponse
}

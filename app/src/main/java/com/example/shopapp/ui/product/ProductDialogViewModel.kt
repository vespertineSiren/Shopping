package com.example.shopapp.ui.product

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.api.ProductApi
import com.example.shopapp.extensions.getData
import com.example.shopapp.extensions.isNumber
import com.example.shopapp.models.User
import com.example.shopapp.models.product.Product
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDialogViewModel @Inject constructor(
    private val productApi: ProductApi,
    private val sharedPreferences: SharedPreferences,
    val gson: Gson
) : ViewModel() {

    private val loggedUser: User?
        get() = sharedPreferences.getData<User>(User.SAVED_LOGGEDIN_USER, gson)

    // Variables
    private var editingProduct: Product? = null
    private var editingProductId: Int? = null
    private lateinit var tempStylesList: List<String>
    private var editedName: String? = null
    private var editedStyle: String? = null
    private var editedBrand: String? = null
    private var editedShipPrice: String? = null
    private var editedDesc: String? = null

    // MutableLiveData
    private val foundProduct by lazy { MutableLiveData<Product>() }
    private val foundStyleIndex by lazy { MutableLiveData<Int>() }
    private val stylesList by lazy { MutableLiveData<List<String>>() }

    // LiveData
    fun getFoundProduct(): LiveData<Product> = foundProduct
    fun getFoundStyleIndex(): LiveData<Int> = foundStyleIndex
    fun getStylesList(): LiveData<List<String>> = stylesList

    suspend fun searchSpinnerStyles() {
        loggedUser?.let { safeUser ->
            val stylesResponse = productApi.getProductStyles(safeUser.bearerToken)
            tempStylesList = stylesResponse.styles
            stylesList.postValue(stylesResponse.styles)
        }
    }

    fun searchProductById(productId: Int) {
        viewModelScope.launch {
            searchSpinnerStyles()

            loggedUser?.let { safeUser ->
                if (productId != -1) {
                    val response = productApi.getProductById(safeUser.bearerToken, productId)
                    editingProduct = response.product
                    editingProductId = response.product.id

                    val foundIndex = tempStylesList.indexOf(response.product.style)
                    foundStyleIndex.postValue(foundIndex)
                    foundProduct.postValue(response.product)
                } else {
                    foundProduct.postValue(null)
                }
            }
        }
    }

    fun handleProductFieldInputs(input: String?, field: String) {
        if (input != null) {
            when (field) {
                NAME -> editedName = input
                STYLE -> editedStyle = input
                BRAND -> editedBrand = input
                SHIP -> if (input.isNumber()) editedShipPrice = input
                DESC -> editedDesc = input
                else -> {}
            }
        }
    }

    fun handleSaveButtonClick() {
        editingProduct?.let {
            viewModelScope.launch {
                verifyProductUpdate()
                loggedUser?.let {
                    productApi.updateProduct(
                        it.bearerToken,
                        editingProduct!!,
                        editingProductId!!
                    )
                }
            }
        } ?: kotlin.run {
            verifyAllFieldsFilled()?.let { query ->
                loggedUser?.let {
                    viewModelScope.launch {
                        productApi.createProduct(it.bearerToken, query)
                    }
                }
            }
        }
    }

    fun verifyProductUpdate() {
        editingProduct?.let { safeProduct ->
            editedName?.let { safeProduct.productName = it }
            editedStyle?.let { safeProduct.style = it }
            editedBrand?.let { safeProduct.brand = it }
            editedShipPrice?.let { safeProduct.shippingPrice = it.toInt() }
            editedDesc?.let { safeProduct.description = it }
        }
    }

    fun verifyAllFieldsFilled(): Product? =
        if (editedName != null &&
            editedStyle != null &&
            editedBrand != null &&
            editedShipPrice != null &&
            editedDesc != null
        ) {
            Product(
                productName = editedName!!,
                style = editedStyle!!,
                brand = editedBrand!!,
                shippingPrice = editedShipPrice!!.toInt(),
                description = editedDesc!!
            )
        } else null

    companion object {
        const val NAME = "name"
        const val STYLE = "style"
        const val BRAND = "brand"
        const val SHIP = "ship"
        const val DESC = "desc"
    }
}

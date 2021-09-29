package com.example.shopapp.ui.product

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.api.ProductApi
import com.example.shopapp.extensions.getData
import com.example.shopapp.models.User
import com.example.shopapp.models.product.Product
import com.example.shopapp.models.product.ProductList
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//  TODO: Use jobs later but simple coroutines for now with scope of project
//  TODO: Use a repository pattern if saving data locally.
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productApi: ProductApi,
    private val sharedPreferences: SharedPreferences,
    val gson: Gson
) : ViewModel() {

    private val loggedUser: User?
        get() = sharedPreferences.getData<User>(User.SAVED_LOGGEDIN_USER, gson)

    // Variables
    private var currentPage: Int = 0
    private var maxPage: Int = 0
    private var userEmail: String = ""
    private var userPassword: String = ""

    // MutableLiveData
    private val productList by lazy { MutableLiveData<List<Product>>() }
    private val disablePreviousButton by lazy { MutableLiveData<Boolean>() }
    private val disableNextButton by lazy { MutableLiveData<Boolean>() }
    private val disableAllButtonsBool by lazy { MutableLiveData<Boolean>() }

    // LiveData
    fun getProductList(): LiveData<List<Product>> = productList
    fun getPreviousButtonBool(): LiveData<Boolean> = disablePreviousButton
    fun getNextButtonBool(): LiveData<Boolean> = disableNextButton
    fun getDisableAllButtonsBool(): LiveData<Boolean> = disableAllButtonsBool

    fun searchInitialProducts() {
        viewModelScope.launch {
            loggedUser?.let { it ->

                //  TODO: Use 25 initially, Make more dynamic later
                val queryMap = ProductList.createQueryMap(page = currentPage, limit = 25)

                val productsResponse = productApi.getProductResponseByQuery(it.bearerToken, queryMap)
                maxPage = productsResponse.pageTotal
                productList.postValue(productsResponse.products)
                disableAllButtonsBool.postValue(true)
                verifyPreviousNextButtonState()
            } ?: run {
                disableAllButtonsBool.postValue(false)
            }
        }
    }

    private fun searchProductsByQuery(query: Map<String, String>) {
        viewModelScope.launch {
            loggedUser?.let { it ->
                val productsResponse = productApi.getProductResponseByQuery(it.bearerToken, query)
                productList.postValue(productsResponse.products)
                verifyPreviousNextButtonState()
            }
        }
    }

    fun handlePreviousButtonClick() {
        currentPage--
        val queryMap = ProductList.createQueryMap(page = currentPage, limit = 25)
        searchProductsByQuery(queryMap)
    }

    fun handleNextButtonClick() {
        currentPage++
        val queryMap = ProductList.createQueryMap(page = currentPage, limit = 25)
        searchProductsByQuery(queryMap)
    }

    fun verifyPreviousNextButtonState() {

        when {
            maxPage == 0 -> {
                disablePreviousButton.postValue(true)
                disableNextButton.postValue(true)
            }
            currentPage == 0 -> {
                disablePreviousButton.postValue(true)
                disableNextButton.postValue(false)
            }
            currentPage == maxPage - 1 -> {
                disablePreviousButton.postValue(false)
                disableNextButton.postValue(true)
            }
            else -> {
                disablePreviousButton.postValue(false)
                disableNextButton.postValue(false)
            }
        }
    }
}

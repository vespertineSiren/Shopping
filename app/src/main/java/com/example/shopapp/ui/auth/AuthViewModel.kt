package com.example.shopapp.ui.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.api.AuthApi
import com.example.shopapp.extensions.getData
import com.example.shopapp.extensions.setData
import com.example.shopapp.models.LoginResponse
import com.example.shopapp.models.User
import com.example.shopapp.models.ValidUser
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Credentials
import javax.inject.Inject

//  TODO: Use jobs later but simple coroutines for now with scope of project
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val sharedPreferences: SharedPreferences,
    val gson: Gson
) : ViewModel() {

    // Variables
    private var userEmail: String = ""
    private var userPassword: String = ""

    // MutableLiveData
    private val loginButtonEnabled by lazy { MutableLiveData<Boolean>() }
    private val logoutButtonEnabled by lazy { MutableLiveData<Boolean>() }
    private val tokenRetrievedResult by lazy { MutableLiveData<String>() }

    // LiveData
    fun getLoginButtonEnabled(): LiveData<Boolean> = loginButtonEnabled
    fun getLogoutButtonEnabled(): LiveData<Boolean> = logoutButtonEnabled
    fun getTokenRetrievedResult(): LiveData<String> = tokenRetrievedResult

    fun searchSavedToken() {
        val user = sharedPreferences.getData<User>(User.SAVED_LOGGEDIN_USER, gson)
        tokenRetrievedResult.postValue(
            user?.let { "Logged In User:\n${it.email}" } ?: "Token Not Found"
        )

        verifyEmailPassword()
        logoutButtonEnabled.postValue(user != null)
    }

    fun handleEmailInput(text: String?) {
        text?.let { safeText ->
            userEmail = safeText
            verifyEmailPassword()
        }
    }

    fun handlePasswordInput(text: String?) {
        text?.let { safeText ->
            userPassword = safeText
            verifyEmailPassword()
        }
    }

    fun handleLoginButtonClick(validUser: ValidUser? = null) {
        viewModelScope.launch {

            //  Check to see if valid user was sent from fragment
            validUser?.let {
                userEmail = it.email
                userPassword = it.password
            }

            val login = authApi.getToken(Credentials.basic(userEmail.trim(), userPassword.trim()))
            if (login.isSuccessful) {
                login.body()?.let {
                    verifyValidToken(it, userEmail, userPassword)
                }
            } else {
                tokenRetrievedResult.postValue(login.message())
            }
        }
    }

    fun handleLogoutButtonClick() {
        sharedPreferences.setData(User.SAVED_LOGGEDIN_USER, null, gson)
        tokenRetrievedResult.postValue("Logged Out")
        userEmail = ""
        userPassword = ""
        logoutButtonEnabled.postValue(false)
        verifyEmailPassword()
    }

    //  Helper Functions

    private fun verifyValidToken(response: LoginResponse, email: String, password: String) {
        if (response.error != 0 || response.token == null) {
            tokenRetrievedResult.postValue(
                "Failed Login\nError Code: ${response.error}\nPossible null token"
            )
            logoutButtonEnabled.postValue(false)
        } else {
            val saveUser = User(email, password, response.token)
            sharedPreferences.setData(User.SAVED_LOGGEDIN_USER, saveUser, gson)
            tokenRetrievedResult.postValue("Logged In User:\n$email")
            loginButtonEnabled.postValue(false)
            logoutButtonEnabled.postValue(true)
        }
    }

    //  TODO: Add better check with Pattern to verify email, password length
    private fun verifyEmailPassword() {
        loginButtonEnabled.postValue(userEmail.isNotEmpty() && userPassword.isNotEmpty())
    }
}

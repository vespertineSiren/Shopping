package com.example.shopapp.models

//  TODO: Implement datastore for sensitive information - password
data class User(
    val email: String,
    val password: String,
    val token: String = ""
) {

    val bearerToken: String
        get() = "Bearer $token"

    companion object {
        val SAVED_LOGGEDIN_USER = "saved logged user"
    }
}

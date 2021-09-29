package com.example.shopapp.extensions

fun String.isNumber(): Boolean =
    try {
        toInt()
        true
    } catch (ex: NumberFormatException) {
        false
    }

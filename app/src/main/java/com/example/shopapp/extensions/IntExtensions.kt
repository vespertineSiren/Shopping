package com.example.shopapp.extensions

import java.util.Locale

fun Int.centsToDollarsFormat(currency: String = "\$"): String {
    val dollars = this / 100
    val cents = this % 100
    return String.format(Locale.US, "%s%d.%02d", currency, dollars, cents)
}

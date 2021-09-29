package com.example.shopapp.extensions

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson

inline fun <reified T> SharedPreferences.getData(key: String, gson: Gson): T? {
    return when (T::class) {
        String::class -> getString(key, null) as? T
        Int::class -> getInt(key, 0) as T
        Long::class -> getLong(key, 0) as T
        Boolean::class -> getBoolean(key, false) as T
        Float::class -> getFloat(key, 0f) as T
        else -> {
            val data = getString(key, null)
            if (data != null && data.isNotEmpty()) {
                gson.fromJson<T>(data)
            } else {
                null
            }
        }
    }
}

fun <T> SharedPreferences.setData(key: String, data: T?, gson: Gson, commit: Boolean = false) {
    edit(commit) {
        if (data == null) {
            remove(key)
        } else {
            when (data) {
                is String -> putString(key, data)
                is Int -> putInt(key, data)
                is Long -> putLong(key, data)
                is Boolean -> putBoolean(key, data)
                is Float -> putFloat(key, data)
                else -> putString(key, gson.toJson(data))
            }
        }
    }
}

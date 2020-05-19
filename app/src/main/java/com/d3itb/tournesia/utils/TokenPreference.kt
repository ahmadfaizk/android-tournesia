package com.d3itb.tournesia.utils

import android.content.Context
import com.d3itb.tournesia.R

class TokenPreference (private val context: Context) {
    companion object {
        private var instance: TokenPreference? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: TokenPreference(context)
            }
    }

    private val sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferense_file_key), Context.MODE_PRIVATE)

    fun saveToken(token: String?) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun getToken() : String? = sharedPreferences.getString("token", null)

    fun removeToken() {
        sharedPreferences.edit().remove("token")
    }
}
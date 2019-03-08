package com.delarosa.cognitoAuth.model

import android.content.Context
import android.content.SharedPreferences

class SharePreferences(private val context: Context) {

    fun setInteger(key: String, value: Int) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInteger(key: String): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getInt(key, 0)
    }

    fun setString(key: String, value: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString(key, "DNF")
    }

    fun setBoolean(key: String, value: Boolean) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean(key, false)
    }

    companion object {

        val PREFS_NAME = "preferenciasApp"
    }
}
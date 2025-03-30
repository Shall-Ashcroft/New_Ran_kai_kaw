package com.example.pos2.utils  // Change to your appropriate package name

import android.content.Context
import android.content.SharedPreferences

class PermissionManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    // Save admin permission status
    fun setAdminPermission(isAdmin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Admin", isAdmin)
        editor.apply() // Save the data asynchronously
    }

    // Check if the user is an admin
    fun isAdmin(): Boolean {
        return sharedPreferences.getBoolean("Admin", false) // Default is false (not admin)
    }
}

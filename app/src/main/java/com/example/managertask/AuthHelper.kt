package com.example.managertask

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns

object AuthHelper {
    private const val PREFS_NAME = "AuthPrefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getCurrentUserEmail(): String? {
        return if (isLoggedIn()) prefs.getString(KEY_EMAIL, null) else null
    }

    fun signInWithEmailAndPassword(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback(false, "Invalid email format")
            return
        }

        // Validate password length
        if (password.length < 6) {
            callback(false, "Password must be at least 6 characters")
            return
        }

        // For demo purposes, accept any valid email/password combination
        // In a real app, you would use Firebase Auth here
        try {
            // Simulate network delay
            Thread.sleep(1000)
            
            // Store credentials locally (for demo purposes only)
            prefs.edit().apply {
                putString(KEY_EMAIL, email)
                putString(KEY_PASSWORD, password)
                putBoolean(KEY_IS_LOGGED_IN, true)
                apply()
            }
            
            callback(true, null)
        } catch (e: Exception) {
            callback(false, "Authentication failed: ${e.message}")
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback(false, "Invalid email format")
            return
        }

        // Validate password length
        if (password.length < 6) {
            callback(false, "Password must be at least 6 characters")
            return
        }

        // For demo purposes, create account with any valid email/password combination
        // In a real app, you would use Firebase Auth here
        try {
            // Simulate network delay
            Thread.sleep(1000)
            
            // Store credentials locally (for demo purposes only)
            prefs.edit().apply {
                putString(KEY_EMAIL, email)
                putString(KEY_PASSWORD, password)
                putBoolean(KEY_IS_LOGGED_IN, true)
                apply()
            }
            
            callback(true, null)
        } catch (e: Exception) {
            callback(false, "Account creation failed: ${e.message}")
        }
    }

    fun signOut() {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }

    fun getCurrentUser(): Any? {
        // For demo purposes, return null since we're not using Firebase
        // In a real app, you would return FirebaseAuth.getInstance().currentUser
        return null
    }
} 
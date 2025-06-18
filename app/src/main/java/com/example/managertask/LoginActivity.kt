package com.example.managertask

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managertask.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize AuthHelper
        AuthHelper.initialize(this)

        // Check if user is already signed in
        if (AuthHelper.isLoggedIn()) {
            navigateToMain()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            performLogin()
        }

        binding.signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordText.setOnClickListener {
            // For now, we'll just show a message. You can implement password reset later
            Toast.makeText(this, "Password reset functionality coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        // Validate input
        if (!validateInput(email, password)) {
            return
        }

        // Show progress
        showProgress(true)

        // Perform authentication using AuthHelper
        CoroutineScope(Dispatchers.IO).launch {
            AuthHelper.signInWithEmailAndPassword(email, password) { success, errorMessage ->
                runOnUiThread {
                    showProgress(false)
                    if (success) {
                        // Sign in success
                        Toast.makeText(this@LoginActivity, "Welcome back, $email!", Toast.LENGTH_SHORT).show()
                        navigateToMain()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@LoginActivity, errorMessage ?: "Authentication failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        // Validate email
        if (email.isEmpty()) {
            binding.emailLayout.error = "Email is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Please enter a valid email"
            return false
        }
        binding.emailLayout.error = null

        // Validate password
        if (password.isEmpty()) {
            binding.passwordLayout.error = "Password is required"
            return false
        }
        if (password.length < 6) {
            binding.passwordLayout.error = "Password must be at least 6 characters"
            return false
        }
        binding.passwordLayout.error = null

        return true
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !show
        binding.signUpButton.isEnabled = !show
        binding.forgotPasswordText.isEnabled = !show
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in and update UI accordingly.
        if (AuthHelper.isLoggedIn()) {
            navigateToMain()
        }
    }
} 
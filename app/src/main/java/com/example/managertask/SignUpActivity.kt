package com.example.managertask

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managertask.databinding.ActivitySignupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize AuthHelper
        AuthHelper.initialize(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.signUpButton.setOnClickListener {
            performSignUp()
        }

        binding.signInButton.setOnClickListener {
            finish() // Go back to login activity
        }
    }

    private fun performSignUp() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        val confirmPassword = binding.confirmPasswordInput.text.toString().trim()

        // Validate input
        if (!validateInput(email, password, confirmPassword)) {
            return
        }

        // Show progress
        showProgress(true)

        // Create user with email and password using AuthHelper
        CoroutineScope(Dispatchers.IO).launch {
            AuthHelper.createUserWithEmailAndPassword(email, password) { success, errorMessage ->
                runOnUiThread {
                    showProgress(false)
                    if (success) {
                        // Sign up success
                        Toast.makeText(this@SignUpActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                        navigateToMain()
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(this@SignUpActivity, errorMessage ?: "Sign up failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
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

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordLayout.error = "Please confirm your password"
            return false
        }
        if (password != confirmPassword) {
            binding.confirmPasswordLayout.error = "Passwords do not match"
            return false
        }
        binding.confirmPasswordLayout.error = null

        return true
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.signUpButton.isEnabled = !show
        binding.signInButton.isEnabled = !show
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
} 
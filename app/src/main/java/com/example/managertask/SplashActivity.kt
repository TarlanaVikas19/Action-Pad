package com.example.managertask

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managertask.databinding.ActivitySplashBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivitySplashBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Initialize AuthHelper
            AuthHelper.initialize(this)

            // Add fade-in animation
            val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            binding.logoImage.startAnimation(fadeIn)
            binding.appNameText.startAnimation(fadeIn)
            binding.taglineText.startAnimation(fadeIn)
            binding.developerText.startAnimation(fadeIn)

            // Set up click listener for the next button
            binding.nextButton.setOnClickListener {
                try {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error launching app: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            // Set up click listener for the logout button
            binding.logoutButton.setOnClickListener {
                performLogout()
            }

            // Show logout button only if user is logged in
            binding.logoutButton.visibility = if (AuthHelper.isLoggedIn()) android.view.View.VISIBLE else android.view.View.GONE

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error initializing splash screen: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun performLogout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                AuthHelper.signOut()
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                binding.logoutButton.visibility = android.view.View.GONE
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
} 
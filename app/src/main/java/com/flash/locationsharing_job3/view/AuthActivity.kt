package com.flash.locationsharing_job3.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flash.locationsharing_job3.databinding.ActivityAuthBinding
import com.flash.locationsharing_job3.repo.UserRepository
import com.flash.locationsharing_job3.viewmodel.AuthViewModel

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    private val repository = UserRepository()

    private val viewModel: AuthViewModel by viewModels<AuthViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {

            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }
            viewModel.login(email,password)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }
            viewModel.register(email,password)
        }

        viewModel.registerResult.observe(this) { (success, message) ->
            if (success) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                navigateToFriendList()
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResult.observe(this) { (success, message) ->
            if (success) {
                Toast.makeText(this, "Logged In Successful", Toast.LENGTH_SHORT).show()
                navigateToFriendList()
            } else {
                Toast.makeText(this, "Logged In Failed", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun navigateToFriendList() {
        val intent = Intent(this, FriendListActivity::class.java)
        startActivity(intent)
        finish()
    }
}
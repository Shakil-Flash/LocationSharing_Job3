package com.flash.locationsharing_job3.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flash.locationsharing_job3.R
import com.flash.locationsharing_job3.databinding.ActivityMyProfileBinding
import com.flash.locationsharing_job3.repo.UserRepository
import com.flash.locationsharing_job3.viewmodel.MyProfileViewModel

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private val repo = UserRepository()

    private val viewModel by viewModels<MyProfileViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyProfileViewModel(repo) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val uid = intent.getStringExtra("uid") ?: return
        val email = intent.getStringExtra("email") ?: ""

        binding.email.text = email
        binding.btnUpdateUsername.setOnClickListener {
            val newName = binding.edtUsername.text.toString().trim()

            if (newName.isEmpty()) {
                Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateUserName(uid, newName) { success, message ->
                if (success) {
                    Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
                    finish() // go back to FriendList
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 🔹 Load user
        viewModel.loadMyProfile(uid)

        viewModel.user.observe(this) { user ->
            user?.let {
                binding.edtUsername.setText(it.userName)
            }
        }

    }
}
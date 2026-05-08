package com.flash.locationsharing_job3.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.locationsharing_job3.repo.UserRepository

class AuthViewModel(private val authRepo: UserRepository): ViewModel() {
    val loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val registerResult = MutableLiveData<Pair<Boolean, String?>>()

    fun login(email: String, password: String) {
        authRepo.loginUser(email,password) { success, message ->
            loginResult.postValue(success to message)
        }
    }

    fun register(email: String, password: String) {
        authRepo.registerUser(email,password) { success, message ->
            registerResult.postValue(success to message)
        }
    }

}
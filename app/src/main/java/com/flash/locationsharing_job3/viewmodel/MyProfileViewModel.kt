package com.flash.locationsharing_job3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.locationsharing_job3.model.AppUser
import com.flash.locationsharing_job3.repo.UserRepository

class MyProfileViewModel(private val myProfileRepo: UserRepository): ViewModel() {
    private val _user = MutableLiveData<AppUser?>()
    val user: LiveData<AppUser?> get() = _user

    fun loadMyProfile(userId: String) {
        myProfileRepo.getUserById(userId) { user ->
            _user.postValue(user)
        }
    }

    fun updateUserName(userId: String, newName: String, onResult: (Boolean, String?) -> Unit) {
        myProfileRepo.updateUsername(userId, newName) { success ->
            onResult(true, "Username Updated")
        }
    }
}
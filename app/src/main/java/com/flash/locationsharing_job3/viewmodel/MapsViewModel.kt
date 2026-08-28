package com.flash.locationsharing_job3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.locationsharing_job3.model.AppUser
import com.flash.locationsharing_job3.repo.UserRepository

class MapsViewModel(private val mapsRepo: UserRepository) : ViewModel() {
    private val _userLocation = MutableLiveData<AppUser?>()
    val userLocation: LiveData<AppUser?> get() = _userLocation

    private val _userList = MutableLiveData<List<AppUser>>()
    val userList: LiveData<List<AppUser>> get() = _userList

    fun loadUserLocation(userId: String) {
        mapsRepo.getUserById(userId) { user ->
            _userLocation.postValue(user)
        }
    }

    fun loadAllUsers() {
        mapsRepo.getAllUsers { users ->
            val currentUid = mapsRepo.getCurrentUserId()
            _userList.postValue(users.filter { it.userId != currentUid })
        }
    }
}
package com.flash.locationsharing_job3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.locationsharing_job3.model.AppUser
import com.flash.locationsharing_job3.repo.UserRepository

class FriendListViewModel(private val friendListRepo: UserRepository): ViewModel() {
    private val _userList = MutableLiveData<List<AppUser>>()
    val userList: LiveData<List<AppUser>> get() = _userList

    fun fetchFriendList() {
        friendListRepo.getAllUsers { users ->
            _userList.postValue(users)
        }
    }

    fun logout() {
        friendListRepo.logout()
    }
}
package com.example.recyclerview1.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview1.model.User
import com.example.recyclerview1.model.UsersListener
import com.example.recyclerview1.model.UsersService

class UsersListViewModel(
    private val usersService: UsersService
): ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val listener: UsersListener = {
        _users.value = it
    }

    init {
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    private fun loadUsers(){
        usersService.addListener(listener)
    }

    fun moveUser(user: User, moveBy: Int){
        usersService.moveUser(user, moveBy)
    }

    fun deleteUser(user: User){
        usersService.deleteUser(user)
    }

    fun swapUsers(startPos: Int, endPos: Int){
        usersService.swapUsers(startPos, endPos)
    }


}
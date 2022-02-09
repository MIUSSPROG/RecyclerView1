package com.example.recyclerview1.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview1.model.DataModel
import com.example.recyclerview1.model.DataModel.User
import com.example.recyclerview1.model.UsersListener
import com.example.recyclerview1.model.UsersService

class UsersListViewModel(
    private val usersService: UsersService
): ViewModel() {

    private val _users = MutableLiveData<List<DataModel>>()
    val users: LiveData<List<DataModel>> = _users

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

    fun moveItem(item: DataModel, moveBy: Int){
        usersService.moveItem(item, moveBy)
    }

    fun deleteItem(item: DataModel){
        usersService.deleteItem(item)
    }

    fun swapItems(startPos: Int, endPos: Int){
        usersService.swapItems(startPos, endPos)
    }

    fun fireItem(item: DataModel){
//        usersService.fireUser(user)
    }

}
package com.example.recyclerview1.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview1.model.User
import com.example.recyclerview1.model.UsersListener
import com.example.recyclerview1.model.UsersService
import com.example.recyclerview1.tasks.*

data class UserListItem(
    val user: User,
    val isInProgress: Boolean
    )

class UsersListViewModel(
    private val usersService: UsersService
): BaseViewModel() {

    private val _users = MutableLiveData<Result<List<UserListItem>>>()
    val users: LiveData<Result<List<UserListItem>>> = _users

    private val userIdsInProgress = mutableSetOf<Long>()
    private var usersResult: Result<List<User>> = EmptyResult()
        set(value) {
            field = value
            notifyUpdates()
        }

    private val listener: UsersListener = {
        usersResult = if (it.isEmpty()){
            EmptyResult()
        }else{
            SuccessResult(it)
        }
    }

    init {
        usersService.addListener(listener)
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    private fun loadUsers(){
        usersResult = PendingResult()
        usersService.loadUsers()
            .onError {
                usersResult = ErrorResult(it)
            }
            .autoCancel()
    }

    fun moveUser(user: User, moveBy: Int){
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
            }
            .autoCancel()
    }

    fun deleteUser(user: User){
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.deleteUser(user)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
            }
            .autoCancel()
    }

    private fun addProgressTo(user: User){
        userIdsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom(user: User){
        userIdsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean{
        return userIdsInProgress.contains(user.id)
    }

    private fun notifyUpdates(){
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }

}
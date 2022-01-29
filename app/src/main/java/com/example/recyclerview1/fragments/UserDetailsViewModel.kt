package com.example.recyclerview1.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview1.UserNotFoundException
import com.example.recyclerview1.model.User
import com.example.recyclerview1.model.UserDetails
import com.example.recyclerview1.model.UsersService
import com.example.recyclerview1.tasks.EmptyResult
import com.example.recyclerview1.tasks.PendingResult
import com.example.recyclerview1.tasks.SuccessResult

class UserDetailsViewModel(
    private val usersService: UsersService
) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val currentState: State get() = state.value!!

    init {
        _state.value = State(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
    }

    fun loadUser(userId: Long){
        if (currentState.userDetailsResult is SuccessResult) return
//        if (_state.value != null) return
//        try {
//            _state.value = usersService.getById(userId)
//        }catch (e: UserNotFoundException){
//            e.printStackTrace()
//        }
    }

    fun deleteUser(){
        val userDetails = this.userDetails.value ?: return
        usersService.deleteUser(userDetails.user)
    }

     data class State(
         val userDetailsResult: Result<UserDetails>,
         private val deletingInProgress: Boolean
     ){
         val showContent: Boolean get() = userDetailsResult is SuccessResult
         val showProgress: Boolean get() = userDetailsResult is PendingResult || deletingInProgress
         val enableDeleteButton: Boolean get() = !deletingInProgress
     }
}
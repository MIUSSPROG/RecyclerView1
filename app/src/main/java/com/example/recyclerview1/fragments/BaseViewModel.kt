package com.example.recyclerview1.fragments

import androidx.lifecycle.ViewModel
import com.example.recyclerview1.tasks.Task

open class BaseViewModel: ViewModel() {

    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        tasks.forEach { it.cancel() }
    }

    fun <T> Task<T>.autoCancel(){
        tasks.add(this)
    }
}
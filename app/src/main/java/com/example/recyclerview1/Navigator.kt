package com.example.recyclerview1

import com.example.recyclerview1.model.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(messageRes: Int)
}
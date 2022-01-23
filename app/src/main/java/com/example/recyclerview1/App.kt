package com.example.recyclerview1

import android.app.Application
import com.example.recyclerview1.model.UsersService

class App : Application(){

    val usersService = UsersService()
}
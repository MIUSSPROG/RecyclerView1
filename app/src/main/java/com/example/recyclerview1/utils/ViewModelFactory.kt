package com.example.recyclerview1.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recyclerview1.App
import com.example.recyclerview1.Navigator
import com.example.recyclerview1.fragments.UserDetailsViewModel
import com.example.recyclerview1.fragments.UsersListViewModel
import java.lang.IllegalStateException

class ViewModelFactory(
    private val app: App
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass){
            UsersListViewModel::class.java -> {
                UsersListViewModel(app.usersService)
            }
            UserDetailsViewModel::class.java ->{
                UserDetailsViewModel(app.usersService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator
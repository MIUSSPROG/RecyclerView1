package com.example.recyclerview1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.recyclerview1.R
import com.example.recyclerview1.adapter.UserActionListener
import com.example.recyclerview1.adapter.UsersAdapter
import com.example.recyclerview1.databinding.FragmentUsersListBinding
import com.example.recyclerview1.model.User
import com.example.recyclerview1.utils.factory
import com.example.recyclerview1.utils.navigator

class UsersListFragment: Fragment(R.layout.fragment_users_list) {

    private lateinit var binding: FragmentUsersListBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel: UsersListViewModel by viewModels{ factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        adapter = UsersAdapter(object : UserActionListener{
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }

            override fun onUserFire(user: User) {

            }

        })

        viewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.users = it
        })

        binding.recyclerView.adapter = adapter

        return binding.root
    }
}
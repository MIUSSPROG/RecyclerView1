package com.example.recyclerview1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recyclerview1.R
import com.example.recyclerview1.databinding.FragmentUserDetailsBinding
import com.example.recyclerview1.utils.factory
import com.example.recyclerview1.utils.navigator

class UserDetailsFragment: Fragment(R.layout.fragment_user_details) {

    private lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: UserDetailsViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getLong(ARG_USER_ID)?.let { viewModel.loadUser(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(layoutInflater, container, false)

        viewModel.userDetails.observe(viewLifecycleOwner, Observer {
            binding.userNameTextView.text = it.user.name
            binding.userDetailsTextView.text = it.details
            if (it.user.photo.isNotBlank()){
                Glide.with(this)
                    .load(it.user.photo)
                    .circleCrop()
                    .into(binding.photoImageView)
            }else{
                Glide.with(this)
                    .load(R.drawable.ic_user_avatar)
                    .into(binding.photoImageView)
            }
        })

        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
            navigator().toast(R.string.user_has_benn_deleted)
            navigator().goBack()
        }

        return binding.root
    }

    companion object{
        private val ARG_USER_ID= "ARG_USER_ID"

        fun createArgs(userId: Long) = bundleOf(ARG_USER_ID to userId)
    }
}
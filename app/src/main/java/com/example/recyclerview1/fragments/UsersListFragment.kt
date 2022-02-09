package com.example.recyclerview1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview1.R
import com.example.recyclerview1.adapter.MultipleViewAdapter
import com.example.recyclerview1.adapter.UserActionListener
import com.example.recyclerview1.databinding.FragmentUsersListBinding
import com.example.recyclerview1.model.DataModel.User
import com.example.recyclerview1.utils.factory
import com.example.recyclerview1.utils.navigator

class UsersListFragment : Fragment(R.layout.fragment_users_list) {

    private lateinit var binding: FragmentUsersListBinding
//    private lateinit var adapter: UsersAdapter
    private lateinit var adapter: MultipleViewAdapter

    private val viewModel: UsersListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        adapter = MultipleViewAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveItem(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteItem(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }

            override fun onUserFire(user: User) {
                viewModel.fireItem(user)
            }

        })

        viewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.adapterData = it
        })

        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags( dragFlags, swipeFlags )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val startPos = source.adapterPosition
                val endPos = target.adapterPosition
                viewModel.swapItems(startPos, endPos)
                recyclerView.adapter?.notifyItemMoved(startPos, endPos)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.adapterData[viewHolder.adapterPosition] as User
                viewModel.deleteItem(itemToDelete)
                Toast.makeText(requireActivity(), "User ${itemToDelete.name} has been deleted", Toast.LENGTH_SHORT).show()
            }


        })


//        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val userToDelete = adapter.users[viewHolder.adapterPosition]
//                viewModel.deleteUser(userToDelete)
//                Toast.makeText(requireActivity(), "User ${userToDelete.name} has been deleted", Toast.LENGTH_SHORT).show()
//            }
//        })
//
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        return binding.root
    }
}
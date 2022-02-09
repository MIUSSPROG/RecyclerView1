package com.example.recyclerview1.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerview1.R
import com.example.recyclerview1.databinding.ItemUserBinding
import com.example.recyclerview1.model.User


interface UserActionListener{
    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)

    fun onUserFire(user: User)
}

class UserListAdapter(
    private val actionListener: UserActionListener
    ): ListAdapter<User, UserListAdapter.UsersViewHolder>(UserItemDiffCallback()), View.OnClickListener {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.imgvMore.setOnClickListener(this)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bindTo(user: User){
            val context = itemView.context
            with(binding){
                itemView.tag = user
                imgvMore.tag = user

                tvUserName.text = user.name
                tvCompany.text = if (user.company.isNotBlank()) user.company else context.getString(
                    R.string.unemployed)
                if (user.photo.isNotBlank()){
                    Glide.with(imgvPhoto.context)
                        .load(user.photo)
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(imgvPhoto)
                }
                else{
                    imgvPhoto.setImageResource(R.drawable.ic_baseline_account_circle_24)
                }
            }
        }
    }

    class UserItemDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

    }

    override fun onClick(v: View) {
        val user = v.tag as User
        when(v.id){
            R.id.imgvMore -> {
                showPopupMenu(v)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as User
        val position = currentList.indexOfFirst { it.id == user.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
            isEnabled = position < currentList.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))
        if (user.company.isNotBlank()){
            popupMenu.menu.add(0, ID_FIRE, Menu.NONE, context.getString(R.string.fire))
        }

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                ID_MOVE_UP -> {
                    actionListener.onUserMove(user, -1)
                }
                ID_MOVE_DOWN -> {
                    actionListener.onUserMove(user, 1)
                }
                ID_REMOVE -> {
                    actionListener.onUserDelete(user)
                }
                ID_FIRE -> {
                    actionListener.onUserFire(user)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    companion object{
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_FIRE = 4
    }

}
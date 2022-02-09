package com.example.recyclerview1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.recyclerview1.R
import com.example.recyclerview1.databinding.ItemStudentBinding
import com.example.recyclerview1.databinding.ItemUserBinding
import com.example.recyclerview1.model.DataModel
import com.example.recyclerview1.model.DataModel.User
import com.example.recyclerview1.model.DataModel.Student

interface UserActionListener {
    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)

    fun onUserFire(user: User)
}

class MultipleViewAdapter(
    private val actionListener: UserActionListener
) : RecyclerView.Adapter<MultipleViewAdapter.DataAdapterViewHolder>() {

    var adapterData: List<DataModel> = ArrayList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class DataAdapterViewHolder(
        var binding: ViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun bindUser(user: User) {
            val userBinding = binding as ItemUserBinding

            with(userBinding) {
                itemView.tag = user
                tvUserName.text = user.name
                val context = userBinding.root.context
                tvCompany.text =
                    if (user.company.isNotBlank()) user.company else context.getString(R.string.unemployed)
                if (user.company.isNotBlank()) {
                    tvCompany.text = user.company
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.no_bg))
                } else {
                    tvCompany.text = context.getString(R.string.unemployed)
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.fire))
                }

                if (user.photo.isNotBlank()) {
                    Glide.with(imgvPhoto.context)
                        .load(user.photo)
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(imgvPhoto)
                } else {
                    imgvPhoto.setImageResource(R.drawable.ic_baseline_account_circle_24)
                }
            }
        }

        private fun bindStudent(student: Student) {
            val studentBinding = binding as ItemStudentBinding

            with(studentBinding) {
                itemView.tag = student
                tvStudentUniversity.text = student.university
                tvStudentGrade.text = student.grade.toString()
                Glide.with(imgvUniversityLogo.context)
                    .load(student.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(imgvUniversityLogo)
            }
        }

        fun bind(dataModel: DataModel) {
            when (dataModel) {
                is User -> bindUser(dataModel)
                is Student -> bindStudent(dataModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_user -> DataAdapterViewHolder(
                ItemUserBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            R.layout.item_student -> DataAdapterViewHolder(
                ItemStudentBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid type")
        }
//        return DataAdapterViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when (adapterData[position]) {
            is User -> R.layout.item_user
            is Student -> R.layout.item_student
        }
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(adapterData[position])
    }

    override fun getItemCount(): Int = adapterData.size

}



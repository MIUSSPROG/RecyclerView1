package com.example.recyclerview1.model

import com.example.recyclerview1.UserNotFoundException
import com.github.javafaker.Faker
import java.util.*
import kotlin.collections.ArrayList
import com.example.recyclerview1.model.DataModel.User
import com.example.recyclerview1.model.DataModel.Student
import kotlin.random.Random
import kotlin.reflect.typeOf

typealias UsersListener = (users: List<DataModel>) -> Unit

class UsersService {

    private var data = mutableListOf<DataModel>()

    private val listeners = mutableSetOf<UsersListener>()

    init {
        val faker = Faker.instance()
        IMAGES.shuffle()
        data = (0..50).map {
            if (it % 2 == 0) {
                User(
                    id = it.toLong(),
                    name = faker.name().name(),
                    company = faker.company().name(),
                    photo = IMAGES[it % IMAGES.size]
                )
            }
            else{
                Student(
                    id = it.toLong(),
                    university = faker.university().name(),
                    grade = Random.nextInt(7),
                    photo = faker.company().logo()
                )
            }

        }.toMutableList()
    }

    fun getUsers(): List<DataModel>{
        return data
    }

//    fun getById(id: Long): UserDetails{
//        val user = data.firstOrNull { it.id == id } ?: throw UserNotFoundException()
//        return UserDetails(
//            user = user,
//            details = Faker.instance().lorem().paragraphs(3).joinToString("\n\n")
//        )
//    }

    fun deleteItem(item: DataModel){
        when(item){
            is User ->{
                var users = data.filterIsInstance<User>()
                val indexToDelete = users.indexOfFirst { it.id == item.id }
                if (indexToDelete != -1){
                    data = ArrayList(data)
                    data.removeAt(indexToDelete)
                    notifyChanges()
                }
            }
            is Student -> {
                var students = data.filterIsInstance<Student>()
                val indexToDelete = students.indexOfFirst { it.id == item.id }
                if (indexToDelete != -1){
                    data = ArrayList(data)
                    data.removeAt(indexToDelete)
                    notifyChanges()
                }
            }
        }


    }

    fun moveItem(item: DataModel, moveBy: Int){
        when(item){
            is User ->{
                var users = data as MutableList<User>
                val oldIndex = users.indexOfFirst { it.id == item.id }
                if (oldIndex == -1) return
                val newIndex = oldIndex + moveBy
                if (newIndex < 0 || newIndex >= users.size) return
                users = ArrayList(users)
                Collections.swap(data, oldIndex, newIndex)
            }
            is Student -> {
                var students = data as MutableList<Student>
                val oldIndex = students.indexOfFirst { it.id == item.id }
                if (oldIndex == -1) return
                val newIndex = oldIndex + moveBy
                if (newIndex < 0 || newIndex >= students.size) return
                students = ArrayList(students)
                Collections.swap(data, oldIndex, newIndex)
            }
        }
        notifyChanges()
    }

    fun swapItems(startPos: Int, endPos: Int){
        Collections.swap(data, startPos, endPos)
        notifyChanges()
    }

//    fun fireUser(user: User){
//        val index = data.indexOfFirst { it.id == user.id }
//        if (index == -1) return
//        val updatedUser = users[index].copy(company = "")
//        users = ArrayList(users)
//        users[index] = updatedUser
//        notifyChanges()
//    }

    fun addListener(listener: UsersListener){
        listeners.add(listener)
        listener.invoke(data)
    }

    fun removeListener(listener: UsersListener){
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach{ it.invoke(data) }
    }

    companion object {
        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1600267185393-e158a98703de?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NjQ0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1579710039144-85d6bdffddc9?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Njk1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODE0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1620252655460-080dbec533ca?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzQ1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1613679074971-91fc27180061?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzUz&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1485795959911-ea5ebf41b6ae?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzU4&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1545996124-0501ebae84d0?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/flagged/photo-1568225061049-70fb3006b5be?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Nzcy&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1567186937675-a5131c8a89ea?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODYx&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1546456073-92b9f0a8d413?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
        )
    }
}
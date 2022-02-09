package com.example.recyclerview1.model

sealed class DataModel{
    data class User (
        val id: Long,
        val photo: String,
        val name: String,
        val company: String
    ) : DataModel()

    data class Student(
        val id: Long,
        val university: String,
        val grade: Int,
        val photo: String
    ) : DataModel()
}




data class UserDetails(
    val user: DataModel.User,
    val details: String
)
package com.example.gymapp.data.repository

import com.example.gymapp.data.dao.UserDao
import com.example.gymapp.data.model.User

class GymRepository (private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun getUser(username: String, password: String) = userDao.getUser(username, password)

}
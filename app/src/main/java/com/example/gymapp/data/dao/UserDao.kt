package com.example.gymapp.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gymapp.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?
}
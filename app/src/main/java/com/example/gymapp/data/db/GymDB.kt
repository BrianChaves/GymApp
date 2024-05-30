package com.example.gymapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gymapp.data.dao.UserDao
import com.example.gymapp.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
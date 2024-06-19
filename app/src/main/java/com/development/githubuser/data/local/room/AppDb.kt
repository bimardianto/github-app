package com.development.githubuser.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.development.githubuser.data.local.entity.UserDao
import com.development.githubuser.data.remote.response.ItemsItem

@Database(entities = [ItemsItem::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
}
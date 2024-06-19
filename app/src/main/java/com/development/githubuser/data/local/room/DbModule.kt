package com.development.githubuser.data.local.room

import android.content.Context
import androidx.room.Room

class DbModule(context: Context) {
    private val db = Room.databaseBuilder(context, AppDb::class.java, userGithub)
        .allowMainThreadQueries()
        .build()

    val userDao = db.userDao()

    companion object {
        private const val userGithub = "user-github.db"
    }
}
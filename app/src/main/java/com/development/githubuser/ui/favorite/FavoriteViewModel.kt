package com.development.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.development.githubuser.data.local.room.DbModule

class FavoriteViewModel(private val dbModule: DbModule) : ViewModel() {
    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(db) as T
        }
    }
    fun getFavoriteUser() = dbModule.userDao.loadAll()
}
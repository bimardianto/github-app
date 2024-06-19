package com.development.githubuser.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.development.githubuser.data.local.room.DbModule
import com.development.githubuser.data.remote.response.ItemsItem
import com.development.githubuser.data.remote.retrofit.ApiConfig
import com.development.githubuser.utils.MessageCallback
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val dbModule: DbModule) : ViewModel() {
    val resultDetailUser = MutableLiveData<MessageCallback>()
    val resultFollowersUser = MutableLiveData<MessageCallback>()
    val resultFollowingUser = MutableLiveData<MessageCallback>()
    val resultFavoriteSuccess = MutableLiveData<Boolean>()
    val resultFavoriteDelete = MutableLiveData<Boolean>()
    private var isFavorite = false

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(db) as T
        }
    }

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig.apiService.getUserDetail(username)
                emit(response)
            }.onStart {
                resultDetailUser.value = MessageCallback.Loading(true)
            }.onCompletion {
                resultDetailUser.value = MessageCallback.Loading(false)
            }.catch {
                resultDetailUser.value = MessageCallback.Error(it)
            }.collect {
                resultDetailUser.value = MessageCallback.Success(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig.apiService.getUserFollowers(username)
                emit(response)
            }.onStart {
                resultFollowersUser.value = MessageCallback.Loading(true)
            }.onCompletion {
                resultFollowersUser.value = MessageCallback.Loading(false)
            }.catch {
                resultFollowersUser.value = MessageCallback.Error(it)
            }.collect {
                resultFollowersUser.value = MessageCallback.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig.apiService.getUserFollowing(username)
                emit(response)
            }.onStart {
                resultFollowingUser.value = MessageCallback.Loading(true)
            }.onCompletion {
                resultFollowingUser.value = MessageCallback.Loading(false)
            }.catch {
                resultFollowingUser.value = MessageCallback.Error(it)
            }.collect {
                resultFollowingUser.value = MessageCallback.Success(it)
            }
        }
    }

    fun setFavorite(item: ItemsItem?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    dbModule.userDao.delete(item)
                    resultFavoriteDelete.value = true
                } else {
                    dbModule.userDao.insert(item)
                    resultFavoriteSuccess.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = dbModule.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

}
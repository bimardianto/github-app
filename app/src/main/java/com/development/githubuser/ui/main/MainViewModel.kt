package com.development.githubuser.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.githubuser.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.development.githubuser.data.preferences.SettingsPreferences
import com.development.githubuser.utils.MessageCallback

class MainViewModel(private val preferences: SettingsPreferences) : ViewModel() {

    val resultUser = MutableLiveData<MessageCallback>()

    class Factory(private val preferences: SettingsPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T
    }

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiConfig.apiService.getUserList()
                emit(response)
            }.onStart {
                resultUser.value = MessageCallback.Loading(true)
            }.onCompletion {
                resultUser.value = MessageCallback.Loading(false)
            }.catch {
                resultUser.value = MessageCallback.Error(it)
            }.collect {
                resultUser.value = MessageCallback.Success(it)
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig.apiService.getUserSearch(
                    mapOf(
                        "q" to username
                    )
                )
                emit(response)
            }.onStart {
                resultUser.value = MessageCallback.Loading(true)
            }.onCompletion {
                resultUser.value = MessageCallback.Loading(false)
            }.catch {
                resultUser.value = MessageCallback.Error(it)
            }.collect {
                resultUser.value = MessageCallback.Success(it.items)
            }
        }
    }

    fun getTheme() = preferences.getThemeSetting().asLiveData()

}
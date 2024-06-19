package com.development.githubuser.utils

sealed class MessageCallback {
    data class Success<out T>(val data: T) : MessageCallback()
    data class Error(val exception: Throwable) : MessageCallback()
    data class Loading(val isLoading: Boolean) : MessageCallback()
}

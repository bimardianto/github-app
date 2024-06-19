package com.development.githubuser.data.remote.retrofit

import com.development.githubuser.data.remote.response.ItemsItem
import com.development.githubuser.data.remote.response.DetailUserResponse
import com.development.githubuser.data.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserList(
    ): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String,
    ): DetailUserResponse

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun getUserSearch(
        @QueryMap params: Map<String, Any>,
    ): UserResponse

    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") username: String,
    ): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String,
    ): MutableList<ItemsItem>

}
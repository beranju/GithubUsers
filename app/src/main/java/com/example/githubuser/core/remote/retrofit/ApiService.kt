package com.example.githubuser.core.remote.retrofit

import com.example.githubuser.core.remote.response.DetailUserResponse
import com.example.githubuser.core.remote.response.SearchUserResponse
import com.example.githubuser.core.remote.response.UserResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun fetchUser(): Response<List<UserResponseItem>>

    @GET("search/users")
    suspend fun fetchUserByLogin(
        @Query("q") login: String
    ): Response<SearchUserResponse>

    @GET("users/{login}")
    suspend fun fetchUserDetail(
        @Path("login") login: String
    ): Response<DetailUserResponse>

    @GET("users/{login}/{follow}")
    suspend fun fetchFollow(
        @Path("login") login: String,
        @Path("follow") follow: String
    ): Response<List<UserResponseItem>>

}
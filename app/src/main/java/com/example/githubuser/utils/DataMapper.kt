package com.example.githubuser.utils

import com.example.githubuser.core.local.entity.UserEntity
import com.example.githubuser.core.remote.response.DetailUserResponse
import com.example.githubuser.core.remote.response.UserResponseItem

object DataMapper {
    fun responseToEntity(user: DetailUserResponse): UserEntity {
        return UserEntity(
            login = user.login.toString(),
            name = user.name.toString(),
            followers = user.followers ?: 0,
            following = user.following ?: 0,
            avatarUrl = user.avatarUrl.toString(),
            url = user.url.toString()
        )
    }

    fun entityToResponse(listUser: List<UserEntity>?): List<UserResponseItem>{
        val newListUser = ArrayList<UserResponseItem>()
        listUser?.map {
            val user = UserResponseItem(
                login = it.login,
                avatarUrl = it.avatarUrl
            )
            newListUser.add(user)
        }
        return newListUser
    }
}
package com.example.githubuser.core.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val login: String,
    val name: String,
    val followers: Int,
    val following: Int,
    val avatarUrl: String,
    val url: String
)

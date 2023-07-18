package com.example.githubuser.core.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.core.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun fetchUser(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login)")
    suspend fun isExists(login: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

}
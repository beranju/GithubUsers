package com.example.githubuser.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.githubuser.core.common.Resource
import com.example.githubuser.core.local.entity.UserEntity
import com.example.githubuser.core.local.room.UserDao
import com.example.githubuser.core.preferences.DataStoreManager
import com.example.githubuser.core.remote.response.DetailUserResponse
import com.example.githubuser.core.remote.response.UserResponseItem
import com.example.githubuser.core.remote.retrofit.ApiService
import com.example.githubuser.utils.DataMapper

class Repository(
    private val apiService: ApiService,
    private val userDao: UserDao,
    val dataStore: DataStoreManager
) {
    fun fetchUser(): LiveData<Resource<List<UserResponseItem>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.fetchUser()
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isEmpty()) {
                        emit(Resource.Empty)
                    }
                    emit(Resource.Success(it))
                }
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun fetchUserByLogin(login: String): LiveData<Resource<List<UserResponseItem>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.fetchUserByLogin(login)
            if (response.isSuccessful) {
                response.body()?.items?.let {
                    if (it.isEmpty()) {
                        emit(Resource.Empty)
                    }
                    emit(Resource.Success(it))
                }
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun fetchDetailUser(login: String): LiveData<Resource<DetailUserResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.fetchUserDetail(login)
            if (response.isSuccessful) {
                if (response.body() == null) {
                    emit(Resource.Empty)
                }
                response.body()?.let {
                    emit(Resource.Success(it))
                }
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun fetchFollow(login: String, follow: String): LiveData<Resource<List<UserResponseItem>>> =
        liveData {
            emit(Resource.Loading)
            try {
                val response = apiService.fetchFollow(login, follow)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isEmpty()) {
                            emit(Resource.Empty)
                        }
                        emit(Resource.Success(it))
                    }
                } else {
                    emit(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    fun fetchFavoriteUser(): LiveData<List<UserEntity>> = userDao.fetchUser()

    suspend fun isExists(login: String): Boolean = userDao.isExists(login)

    suspend fun insertUser(user: DetailUserResponse) {
        DataMapper.responseToEntity(user).let {
            userDao.insert(it)
        }
    }

    suspend fun deleteUser(user: DetailUserResponse) {
        DataMapper.responseToEntity(user).let {
            userDao.delete(it)
        }
    }

    companion object {
        @Volatile
        var instance: Repository? = null

        fun getInstance(apiService: ApiService, userDao: UserDao, dataStore: DataStoreManager): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userDao, dataStore)
            }.also { instance = it }
    }

}

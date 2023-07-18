package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.core.Repository
import com.example.githubuser.core.local.room.UserDatabase
import com.example.githubuser.core.preferences.DataStoreManager
import com.example.githubuser.core.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository{
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val userDao = database.userDao()
        val dataStore = DataStoreManager(context)
        return Repository.getInstance(apiService, userDao, dataStore)
    }
}
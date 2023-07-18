package com.example.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.githubuser.core.Repository
import com.example.githubuser.core.remote.response.UserResponseItem
import com.example.githubuser.utils.DataMapper

class FavoriteViewModel(private val repository: Repository) : ViewModel() {

    suspend fun fetchUser(): LiveData<List<UserResponseItem>> {
        val user = repository.fetchFavoriteUser()
        return Transformations.map(user) { list ->
            DataMapper.entityToResponse(list)
        }
    }

}
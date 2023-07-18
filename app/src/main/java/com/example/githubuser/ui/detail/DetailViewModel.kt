package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.core.Repository
import com.example.githubuser.core.remote.response.DetailUserResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private var _isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun fetchDetailUser(login: String) = repository.fetchDetailUser(login)

    fun fetchFollow(login: String, follow: String) = repository.fetchFollow(login, follow)

    fun setFavoriteUser(user: DetailUserResponse){
        viewModelScope.launch {
            if (isFavorite.value == true){
                repository.deleteUser(user)
            }else{
                repository.insertUser(user)
            }
            isExists(user.login.toString())
        }
    }

    fun isExists(login: String) {
        viewModelScope.launch {
            _isFavorite.value = repository.isExists(login)
        }
    }
}
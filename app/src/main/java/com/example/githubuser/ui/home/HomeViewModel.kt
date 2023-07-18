package com.example.githubuser.ui.home

import androidx.lifecycle.ViewModel
import com.example.githubuser.core.Repository

class HomeViewModel(private val repository: Repository) : ViewModel() {
    val result = repository.fetchUser()

    fun fetchUserByLogin(login: String) = repository.fetchUserByLogin(login)
}
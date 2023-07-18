package com.example.githubuser.ui.setting

import androidx.lifecycle.*
import com.example.githubuser.core.Repository
import com.example.githubuser.core.preferences.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: Repository): ViewModel() {
    private var _user : MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> get() = _user

    val getTheme = repository.dataStore.getTheme().asLiveData()

    init {
        getUser()
    }

    private fun getUser(){
        viewModelScope.launch {
            repository.dataStore.getData().collectLatest {
                _user.value = it
            }
        }
    }

    fun saveProfile(name: String) {
        viewModelScope.launch {
            repository.dataStore.saveData(name)
        }
    }

    fun setTheme(dark: Boolean){
        viewModelScope.launch {
            repository.dataStore.setTheme(dark)
            getTheme
        }
    }
}
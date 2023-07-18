package com.example.githubuser.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
    private val Context.themePrefs: DataStore<Preferences> by preferencesDataStore("theme")

    companion object{
        val NAME = stringPreferencesKey("name")
        val IS_DARK = booleanPreferencesKey("isDark")
    }

    suspend fun setTheme(dark: Boolean){
        context.themePrefs.edit {
            it[IS_DARK] = dark
        }
    }

    fun getTheme() = context.themePrefs.data.map {
        it[IS_DARK] ?: false
    }

    suspend fun saveData(name: String){
        context.dataStore.edit {
            it[NAME] = name
        }
    }

    fun getData() = context.dataStore.data.map {
        User(it[NAME] ?: "")
    }
}
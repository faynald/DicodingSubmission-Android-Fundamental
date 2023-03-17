package com.farhanrv.githubuser.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DarkModePreferences private constructor(private val dataStore: DataStore<Preferences>){

    fun isDarkMode(): Flow<Boolean> {
        return dataStore.data.map { preferences -> preferences[THEME_KEY]  ?: false}
    }

    suspend fun changeTheme(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("DarkMode_Setting")

        @Volatile
        private var INSTANCE: DarkModePreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>) : DarkModePreferences {
            return INSTANCE ?: synchronized(this) {
                val temporalInstance = DarkModePreferences(dataStore)
                INSTANCE = temporalInstance
                temporalInstance
            }
        }
    }
}

package com.farhanrv.githubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farhanrv.githubuser.data.local.DarkModePreferences
import com.farhanrv.githubuser.ui.main.MainViewModel

class SettingFactory(private val pref: DarkModePreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
    }
}

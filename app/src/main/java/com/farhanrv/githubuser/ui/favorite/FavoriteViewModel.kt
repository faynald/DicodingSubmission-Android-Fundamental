package com.farhanrv.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.farhanrv.githubuser.data.AppRepository
import com.farhanrv.githubuser.data.network.response.UserItem

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: AppRepository = AppRepository(application)

    fun getFavorites(): LiveData<List<UserItem>> = mFavoriteUserRepository.getFavorites()
}
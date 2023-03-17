package com.farhanrv.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.farhanrv.githubuser.data.local.AppDao
import com.farhanrv.githubuser.data.local.AppDatabase
import com.farhanrv.githubuser.data.network.response.UserItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppRepository(application: Application) {
    private val appDao: AppDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AppDatabase.getDatabase(application)
        appDao = db.appDao()
    }
    fun getFavorites(): LiveData<List<UserItem>> = appDao.getAllFavorites()

    fun insert(user: UserItem) {
        executorService.execute { appDao.insert(user) }
    }

    fun delete(user: UserItem) {
        executorService.execute { appDao.delete(user) }
    }

    fun ifExist(login: String) = appDao.ifExist(login)
}
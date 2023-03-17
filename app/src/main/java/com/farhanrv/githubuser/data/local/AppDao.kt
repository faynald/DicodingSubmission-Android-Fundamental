package com.farhanrv.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.farhanrv.githubuser.data.network.response.UserItem

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: UserItem)

    @Delete
    fun delete(item: UserItem)

    @Query("SELECT * FROM user ORDER BY db_id ASC")
    fun getAllFavorites(): LiveData<List<UserItem>>

    @Query("SELECT COUNT(login) FROM user WHERE login = :login")
    fun ifExist(login: String?): LiveData<Boolean>
}

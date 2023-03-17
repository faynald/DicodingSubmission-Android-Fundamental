package com.farhanrv.githubuser.data.network.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchResponse(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<UserItem>? = null
)

@Entity(tableName = "user")
@Parcelize
data class UserItem(

	@PrimaryKey
	@ColumnInfo(name = "db_id")
	@field:SerializedName("id")
	val id: Int? = null,

	@ColumnInfo(name = "login")
	@field:SerializedName("login")
	val login: String? = null,

	@ColumnInfo(name = "url")
	@field:SerializedName("url")
	val url: String? = null,

	@ColumnInfo(name = "avatar_url")
	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@ColumnInfo(name = "gravatar_id")
	@field:SerializedName("gravatar_id")
	val gravatarId: String? = null,
) : Parcelable

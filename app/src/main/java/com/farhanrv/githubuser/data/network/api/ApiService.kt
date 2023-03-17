package com.farhanrv.githubuser.data.network.api

import com.farhanrv.githubuser.BuildConfig
import com.farhanrv.githubuser.data.network.response.DetailResponse
import com.farhanrv.githubuser.data.network.response.FollowResponse
import com.farhanrv.githubuser.data.network.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers(auth)
    fun searchUser(@Query("q") query: String?): Call<SearchResponse>

    @GET("users/{username}")
    @Headers(auth)
    fun getDetailUser(@Path("username") username: String?): Call<DetailResponse>

    @GET("users/{username}/following")
    @Headers(auth)
    fun getFollowing(@Path("username") username: String?): Call<ArrayList<FollowResponse?>?>

    @GET("users/{username}/followers")
    @Headers(auth)
    fun getFollowers(@Path("username") username: String?): Call<ArrayList<FollowResponse?>?>

    companion object {
        const val auth = BuildConfig.KEY
    }
}
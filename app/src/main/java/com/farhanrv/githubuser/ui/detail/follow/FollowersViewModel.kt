package com.farhanrv.githubuser.ui.detail.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhanrv.githubuser.data.network.api.ApiConfig
import com.farhanrv.githubuser.data.network.api.ApiService
import com.farhanrv.githubuser.data.network.response.FollowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val followers: MutableLiveData<ArrayList<FollowResponse?>> =
        MutableLiveData<ArrayList<FollowResponse?>>()
    private val _isLoading = MutableLiveData<Boolean>()
    fun getFollowers(): LiveData<ArrayList<FollowResponse?>> {
        return followers
    }

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun setFollowerUser(username: String?) {
        _isLoading.value = true
        val apiService: ApiService = ApiConfig.getApiService()
        val modelFollowItemCall: Call<ArrayList<FollowResponse?>?> =
            apiService.getFollowers(username)
        modelFollowItemCall.enqueue(object : Callback<ArrayList<FollowResponse?>?> {
            override fun onResponse(
                call: Call<ArrayList<FollowResponse?>?>,
                response: Response<ArrayList<FollowResponse?>?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        followers.value = response.body()
                    }
                } else {
                    if (response.body() != null) {
                        Log.e(TAG, "incomplete result: " + response.body())
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowResponse?>?>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: " + t.message)
            }
        })
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }
}

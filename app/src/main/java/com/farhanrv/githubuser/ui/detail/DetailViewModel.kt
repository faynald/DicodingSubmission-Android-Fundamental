package com.farhanrv.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhanrv.githubuser.data.AppRepository
import com.farhanrv.githubuser.data.network.api.ApiConfig
import com.farhanrv.githubuser.data.network.api.ApiService
import com.farhanrv.githubuser.data.network.response.DetailResponse
import com.farhanrv.githubuser.data.network.response.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val viewModelTag = "DetailViewModel"

    private val _userData: MutableLiveData<DetailResponse> =
        MutableLiveData<DetailResponse>()

    private val _isLoading = MutableLiveData<Boolean>()

    fun getUser(): LiveData<DetailResponse?> {
        return _userData
    }

    fun isLoading(): LiveData<Boolean> {
        return _isLoading
    }

    private val mFavoriteUserRepository: AppRepository =
        AppRepository(application)

    fun insert(user: UserItem) {
        mFavoriteUserRepository.insert(user)
    }
    fun delete(user: UserItem){
        mFavoriteUserRepository.delete(user)
    }
    fun ifExist(login: String): LiveData<Boolean> = mFavoriteUserRepository.ifExist(login)

    fun setUserDetail(username: String?) {
        _isLoading.value = true
        val apiService: ApiService = ApiConfig.getApiService()
        val modelUserCall: Call<DetailResponse> = apiService.getDetailUser(username)
        modelUserCall.enqueue(object : Callback<DetailResponse?> {
            override fun onResponse(
                call: Call<DetailResponse?>,
                response: Response<DetailResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _userData.value = response.body()
                    }
                } else {
                    if (response.body() != null) {
                        Log.e(viewModelTag, "incomplete result: " + response.body())
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse?>, t: Throwable) {
                _isLoading.value = false
                Log.e(viewModelTag, "onFailure: " + t.message)
            }
        })
    }
}
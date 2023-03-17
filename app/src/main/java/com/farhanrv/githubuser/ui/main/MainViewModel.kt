package com.farhanrv.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.farhanrv.githubuser.data.local.DarkModePreferences
import com.farhanrv.githubuser.data.network.api.ApiConfig
import com.farhanrv.githubuser.data.network.response.UserItem
import com.farhanrv.githubuser.data.network.response.SearchResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preferences: DarkModePreferences) : ViewModel() {
    private val _searchResult = MutableLiveData<ArrayList<UserItem>>()
    private val _isLoading = MutableLiveData<Boolean>()

    val searchResult: LiveData<ArrayList<UserItem>>
        get() = _searchResult
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun findUser(keyword: String?) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val client = apiService.searchUser(keyword)
        client.enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>,
                response: Response<SearchResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val items = response.body()!!.items?.let {
                            ArrayList(
                                it
                            )
                        }
                        if (items != null) {
                            _searchResult.postValue(items)
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Log.e(TAG, "incomplete result: " + response.body())
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: " + t.message)
            }
        })
    }

    fun isDarkMode(): LiveData<Boolean> {
        return preferences.isDarkMode().asLiveData()
    }

    fun changeTheme(isDarkModeAcitve: Boolean){
        viewModelScope.launch {
            preferences.changeTheme(isDarkModeAcitve)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
package com.example.recyclersample.detailPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recyclersample.api.ApiConfig
import com.example.recyclersample.api.UserDetail
import com.example.recyclersample.data.PersonFave
import com.example.recyclersample.data.PersonFaveSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonDetailViewModel (application: Application, username: String): AndroidViewModel(application) {
    private val mPersonFaveRepository : PersonFaveSource = PersonFaveSource(getApplication())
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()
    private val _detailUser =  MutableLiveData<UserDetail>()
    private val _followersList = MutableLiveData<List<UserDetail>>()
    private val _followingList = MutableLiveData<List<UserDetail>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isError : LiveData<Boolean> = _isError
    val detailUser: LiveData<UserDetail> = _detailUser
    val followersList : LiveData<List<UserDetail>> = _followersList
    val followingList : LiveData<List<UserDetail>> = _followingList
    val faveExist : LiveData<Boolean> = mPersonFaveRepository.getPersonFaveByUsername(username)

    fun doneToastErrorInput() { _isError.value = false }
    fun addFave(favoriteUser: PersonFave) { mPersonFaveRepository.insert(favoriteUser) }
    fun deleteFave(favoriteUser: PersonFave) { mPersonFaveRepository.delete(favoriteUser) }
    fun checkIfFaveExist() : Boolean? { return faveExist.value }

    fun displayUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object: Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _detailUser.value = response.body()
                }
            }
            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    fun displayFollowersList(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserDetail>> {
            override fun onResponse(call: Call<List<UserDetail>>, response: Response<List<UserDetail>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _followersList.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<UserDetail>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    fun displayFollowingList(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserDetail>> {
            override fun onResponse(call: Call<List<UserDetail>>, response: Response<List<UserDetail>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _followingList.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<UserDetail>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}